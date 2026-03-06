package com.nantian.nbp.main.controller;

import com.nantian.nbp.base.model.FTranKey;
import com.nantian.nbp.base.model.TranCodeConv;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.flow.engine.service.api.ExceptionResHandler;
import com.nantian.nbp.flow.engine.service.api.FlowEngineService;
import com.nantian.nbp.flow.engine.service.api.FlowResult;
import com.nantian.nbp.flow.engine.service.api.PbSequenceHandler;
import com.nantian.nbp.flow.engine.service.api.context.PbHeader;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.log4j2.TranLogConstants;
import com.nantian.nbp.utils.JsonUtil;
import com.nantian.nbp.utils.StrUtils;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.nantian.nbp.flow.engine.service.api.FeConstants.ERR_EXCEPTION_TYPE;
import static com.nantian.nbp.log4j2.TranLogConstants.TRACE_GID;

/**
 * @author Administrator
 */
@RequestMapping("tran")
@Controller
public class FlowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowController.class);

    private final CacheClientApi cacheClientApi;

    private final FlowEngineService<HttpServletResponse> flowEngineService;

    private final ExceptionResHandler exceptionResHandler;

    private final PbSequenceHandler pbSequenceHandler;

    @Value("${spring.application.name}")
    private String serverName;

    @Value("${server.port}")
    private String port;

    public FlowController(CacheClientApi cacheClientApi, FlowEngineService<HttpServletResponse> flowEngineService,
                          ExceptionResHandler exceptionResHandler, PbSequenceHandler pbSequenceHandler) {
        this.cacheClientApi = cacheClientApi;
        this.flowEngineService = flowEngineService;
        this.exceptionResHandler = exceptionResHandler;
        this.pbSequenceHandler = pbSequenceHandler;
    }

    @PostMapping("/{chnlNo}/{fTranCode}")
    public void flow(@PathVariable("chnlNo") String chnlNo,
                       @PathVariable("fTranCode") String fTranCode, @RequestBody String reqMsg,
                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        flowP(new FTranKey(chnlNo, fTranCode), request, response, reqMsg);
    }

    private void flowP(FTranKey ftk, HttpServletRequest request, HttpServletResponse response, String reqMsg) throws IOException {
        String reqUri = request.getRequestURI();
        // 接受header信息
        Map<String, String> headerMap = receiveHeader(request);

        String bizId = StrUtils.toStrDefNull(headerMap.get(TRACE_GID));
        if(Objects.isNull(bizId)){
            bizId = pbSequenceHandler.generate();
        }

        MDC.put(TranLogConstants.UUID, bizId);
        FlowResult flowResult = new FlowResult(bizId);
        TranCodeConv tcc = cacheClientApi.getTranCodeConv(ftk);
        if (!ObjectUtils.isEmpty(tcc) && StringUtils.hasText(bizId)) {
            // 登记流程执行开始时间
            Long startTime = Timer.getStartTime();
            LOGGER.info("===================Tran Begin [{}]===================", bizId);
            LOGGER.info("[{}]服务请求[{}],对应内部流程[{}_{}]", serverName, reqUri, tcc.getBusiType(), tcc.getTranCode());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("receive client request httpHeader [{}]", JsonUtil.objToString(headerMap));
            }
            LOGGER.debug("receive client request message [{}]", reqMsg);
            try {
                flowResult = flowEngineService.executeService(tcc, bizId, headerMap, reqMsg, response);
            } finally {
                response.setCharacterEncoding("UTF-8");
                // 临时使用AreaNo字段
                if(Objects.equals("text",tcc.getAreaNo())){
                    response.setContentType("text/plain");
                }else {
                    response.setContentType("application/json");
                }
                response.setHeader("traceId", bizId);
                if (Objects.nonNull(flowResult)) {
                    PbHeader outHeader = flowResult.getOutHeader();
                    if (!ObjectUtils.isEmpty(outHeader)) {
                        outHeader.forEach(response::setHeader);
                    }
                }
                LOGGER.info("===================Tran End [{}],acctNo[{}],instRespCode[{}],usedTime[{}]ms ===================",
                        bizId, flowResult.getAcctNo(), flowResult.getInstRespCode(), Timer.getUsedTime(startTime));
            }
        } else {
            tcc = new TranCodeConv();
            FlowException exception = new FlowException(FlowException.EFLW001, ftk.toString(),
                    String.format("[%s]_" + "[%s] tranCode not found.", ftk.getChnlNo(), ftk.getfTranCode()),null);
            tcc.setfTranCode(ftk.getfTranCode());
            tcc.setChnlNo(ftk.getChnlNo());
            String errRes = exceptionResHandler.exceptionRes(bizId, exception, tcc, response, ERR_EXCEPTION_TYPE, null);
            flowResult.setRes(errRes);
            flowResult.setRetType(RetType.FAILED);
        }
        String res = Optional.of(flowResult).map(FlowResult::getRes).orElse("{\"msg\":null}");
        response.getOutputStream()
                .write(res.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 循环接收header
     *
     * @param request 请求对象
     * @return Map<String, Object> 请求HTTP报文头
     */
    private Map<String, String> receiveHeader(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>(16);
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            map.put(key, request.getHeader(key));
        }
        return map;
    }


}
