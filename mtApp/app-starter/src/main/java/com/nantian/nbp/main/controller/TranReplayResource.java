package com.nantian.nbp.main.controller;

import com.nantian.nbp.base.model.FTranKey;
import com.nantian.nbp.base.model.TranCodeConv;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.flow.engine.service.api.FlowEngineService;
import com.nantian.nbp.flow.engine.service.api.FlowResult;
import com.nantian.nbp.flow.engine.service.api.PbSequenceHandler;
import com.nantian.nbp.log4j2.TranLogConstants;
import com.nantian.nbp.main.model.TranReplay;
import com.nantian.nbp.main.po.TPbTranReplay;
import com.nantian.nbp.main.service.ITranReplayInfoService;
import com.nantian.nbp.utils.JsonUtil;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * @author Administrator
 */
@RestController
public class TranReplayResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TranReplayResource.class);
    protected final HttpServletRequest request;
    protected final HttpServletResponse response;
    @Resource
    private CacheClientApi cacheClientApi;
    @Resource
    private FlowEngineService<HttpServletResponse> flowEngineService;
    @Autowired
    private ITranReplayInfoService iTranReplayInfoService;
    @Resource
    private PbSequenceHandler pbSequenceHandler;
    @Value("${spring.application.name}")
    private String serverName;
    @Value("${server.port}")
    private String port;

    public TranReplayResource(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @PostMapping("/replay")
    public void flow(@RequestBody TranReplay tranReplay) {
        if (!ObjectUtils.isEmpty(tranReplay.getIds())) {
            for (Integer id : tranReplay.getIds()) {
                flowP(id, response);
            }
        }
    }

    /**
     * 处理流量传输的重放流程
     * 此方法启动一个计时器，获取业务ID，并初始化流程结果对象，
     * 使用流量传输键（包含渠道号和功能代码）来准备重放服务请求的URL，
     * 使用参数映射进行数据库查询，并调用引擎执行服务，
     * 最后记录重放流程的结果和耗时。
     *
     * @param id 重放记录主键
     */
    private void flowP(int id, HttpServletResponse response) {
        // 登记流程重放开始时间
        Long startTime = Timer.getStartTime();
        // 获取当前业务ID
        String bizId = pbSequenceHandler.generate();
        // 初始化流程结果对象
        FlowResult flowResult = new FlowResult(bizId);
        // 实例化重放信息对象
        TPbTranReplay tPbTranReplay = new TPbTranReplay();
        // 构造重放服务请求的URL后缀
        String reqUri = null;
        try {
            if (!StringUtils.hasText(bizId)) {
                throw new RuntimeException("bizId is null");
            }
            // 查询数据库以获取重放信息
            tPbTranReplay = iTranReplayInfoService.selectTranReplayInfo(id);
            // 确保交易代码转换信息和重放信息存在
            if (!ObjectUtils.isEmpty(tPbTranReplay)) {
                reqUri = tPbTranReplay.getReqChnl() + "/" + tPbTranReplay.getfTranCode();
                FTranKey ftk = new FTranKey(tPbTranReplay.getReqChnl(), tPbTranReplay.getfTranCode());
                // 获取交易代码转换信息
                TranCodeConv tcc = cacheClientApi.getTranCodeConv(ftk);
                logBefore(bizId, tcc);
                if (!ObjectUtils.isEmpty(tcc)) {
                    // 记录重放服务请求的日志信息
                    LOGGER.info("===================TranReplay Begin [{}]===================", bizId);
                    LOGGER.info("[{}]重放服务请求[{}],对应内部流程[{}_{}]", serverName, reqUri, tcc.getBusiType(), tcc.getTranCode());
                    // 处理header信息，使用 FastJSON 解析 JSON 到 Map
                    Map<String, String> headerMap = JsonUtil.strToObj(tPbTranReplay.getReqHeader(), Map.class);
                    LOGGER.info("receive client request httpHeader [{}]", JsonUtil.objToString(headerMap));
                    LOGGER.info("receive client request message [{}]", tPbTranReplay.getReqMsg());
                    // 执行服务并获取结果
                    flowResult = flowEngineService.executeService(tcc, bizId, headerMap, tPbTranReplay.getReqMsg(), response);
                }
            }
        } catch (Exception e) {
            LOGGER.error("[{}]重放服务请求[{}]异常,异常信息[{}]", serverName, reqUri, e.getMessage(),e);
        } finally {
            // 记录流程结束和耗时的日志信息
            LOGGER.info("===================TranReplay End [{}], usedTime[{}]ms ===================", bizId, Timer.getUsedTime(startTime));
            // 更新重放信息到数据库，如果结果非空
            if (Objects.nonNull(flowResult)) {
                tPbTranReplay.setReplayResult(flowResult.getRes());
                updateTranReplayInfo(tPbTranReplay);
            }
            logAfter();
        }
    }

    private void updateTranReplayInfo(TPbTranReplay tPbTranReplay){
        try {
            iTranReplayInfoService.updateTranReplayInfo(tPbTranReplay);
        } catch (Exception e) {
            LOGGER.error("重放信息更新异常[{}]", tPbTranReplay.getId(),e);
        }
    }

    private void logBefore(String bizId, TranCodeConv tcc) {
        String chnlNo = tcc.getChnlNo();
        String fTranCode = tcc.getfTranCode();
        MDC.put(TranLogConstants.LOG_TYPE, TranLogConstants.REPLAY_TYPE);
        MDC.put(TranLogConstants.UUID, bizId);
        MDC.put(TranLogConstants.LOG_KEY, TranLogConstants.REPLAY_TYPE + chnlNo + "_" + fTranCode);
        MDC.put(TranLogConstants.LOG_CHNL_NO, chnlNo);
        MDC.put(TranLogConstants.LOG_TRAN_CODE, fTranCode);
        MDC.put(TranLogConstants.POIN_PB_LEVEL, tcc.getLogLevel());
    }

    private void logAfter() {
        MDC.clear();
    }
}
