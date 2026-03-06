package com.n.fbsp.atom.platform.seed.other.atom.comm;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 指定位数随机数生成
 *
 * @author 
 */
@Atom("fbsp.RandomNumGenerate")
public class RandomNumGenerateImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomNumGenerateImpl.class);

    /**
     * 根据指定参数生成随机数并返回结果
     * 当前作用域key|随机数位数不带引号
     * 示例：randomNum|5
     *
     * @param tranContext
     * @param scopeValUnit 流程上下文对象，用于传递流程估值单元和外部服务交互时所需的上下文信息
     * @param flowUnit     当前单元，提供原子事务参数和外部服务交互时所需的上下文信息
     * @return Result对象，包含处理结果和数据
     *
     * @throws FlowException 流程异常，当流程执行过程中发生错误时抛出
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化处理结果对象
        AtomResult atomResult = new AtomResult();
        /*if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取流程参数字符串
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 将参数字符串按分隔符分割成数组
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 参数校验：判断参数个数是否正确
        if (atomTranParams.length != PARAM_NUM_TWO) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为2,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数个数不为2,请检查参数配置[{}]", atomTranParam), null);
        }
        // 解析参数：随机数键名和长度
        String key = MapUtils.spiltVal(atomTranParams[0]);
        int length = (Integer.parseInt(atomTranParams[1]));
        // 参数校验：判断随机数长度是否合法
        if (length <= 0) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "随机数长度不合法,请检查参数配置[{}]", atomTranParams[1]);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("随机数长度不合法,请检查参数配置[{}]", atomTranParams[1]), null);
        }
        try {
            // 获取当前时间戳
            LocalDateTime now = LocalDateTime.now();
            Instant instantNow = now.atZone(ZoneId.systemDefault()).toInstant();
            long unixTimestamp = instantNow.getEpochSecond();
            // 生成随机数
            String randomNumber = RandomUtil.randomNumbers(length);
            // 日志记录：生成的随机数值
            LOGGER.debug("生成的随机数为 [{}]", randomNumber);
            // 将随机数和时间戳保存到上下文对象中
            MapUtils.setVal(scopeValUnit, key, randomNumber);
            MapUtils.setVal(scopeValUnit, "sendSeconds", String.valueOf(unixTimestamp));
            MapUtils.setVal(scopeValUnit, "msgSendtime", DateUtil.formatDate2Str(new Date(), DateUtil.PATTERN_14));
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "生成随机数异常,异常信息[{}]", e.getMessage(), e);
            //调用异常时的降级处理
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("生成随机数异常,异常信息[{}]", e.getMessage()), e);
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit.toString());*/
        // 设置处理结果为成功
        atomResult.setRetType(RetType.SUCCESS);
        // 返回处理结果
        return atomResult;
    }


}