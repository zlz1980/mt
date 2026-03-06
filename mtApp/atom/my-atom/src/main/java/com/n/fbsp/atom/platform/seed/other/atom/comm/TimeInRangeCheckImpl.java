package com.n.fbsp.atom.platform.seed.other.atom.comm;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 时间范围判断
 *
 * @author
 */
@Atom("fbsp.TimeInRangeCheck")
public class TimeInRangeCheckImpl implements AtomService {
    public static final String PATTERN_FORMAT = "yyyyMMddHHmmss";
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeInRangeCheckImpl.class);
    private static final String OPERATOR_HOUR = "HH";
    private static final String OPERATOR_MINUTE = "mm";
    private static final String OPERATOR_SECOND = "ss";

    /**
     * 判断给定时间是否在当前时间之后，并根据判断结果设置处理结果状态
     * 示例：用于验证某个日期是否在当前时间之后，并根据验证结果设置原子交易的结果类型
     * 参数1：日期时间格式，例如：yyyyMMddHHmmss，用于指定待比较的日期时间参数示例：“${req.date}”
     * 参数2：数值，用于增加或减少指定时间单位 示例：“1”，“-1”
     * 参数3：时间单位类型，支持类型包括：HH（小时），mm（分钟），ss（秒）
     *
     * @param tranContext
     * @param scopeValUnit 流程上下文对象，包含执行过程中的上下文信息
     * @param flowUnit     当前工作单元，执行具体业务逻辑的最小单元
     * @return 返回原子结果对象，包含本次操作的结果状态和信息
     *
     * @throws FlowException 流程异常，当参数不正确或处理失败时抛出异常
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化原子结果对象，用于记录执行结果
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取流程中的原子交易参数，用于判断是否需要执行服务
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 参数分割，将参数字符串分割为数组进行处理
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 参数长度非三个则抛出异常，参数校验不通过则退出服务执行
        if (atomTranParams.length != PARAM_NUM_THREE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam), null);
        }
        // 定义判断标志变量，用于时间比较结果存储
        boolean flag;
        // 定义时间格式化工具，用于日期时间的解析和格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
        // 获取当前时间，用于时间比较基准
        LocalDateTime now = LocalDateTime.now();
        // 尝试解析日期时间参数并执行时间比较逻辑，任何异常将导致流程异常抛出
        try {
            // 解析开始日期字符串，移除不需要的字符并解析为日期时间格式
            String startDateStr = tranContext.getCtxVal(StrUtils.trim(atomTranParams[0])).getVal().toString()
                                             .replaceAll("[ " + ":T]", "");
            // 解析开始日期字符串为LocalDateTime对象，用于时间运算和比较
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
            // 获取操作类型参数，用于确定时间增加或减少的方向和数量
            String operator = tranContext.getValue(StrUtils.trim(atomTranParams[2])).toString();
            // 获取时间范围参数，支持正负数和不同时间单位的运算操作
            int timeRange = Integer.parseInt(tranContext.getValue(StrUtils.trim(atomTranParams[1])).toString());
            // 根据操作类型执行相应的时间运算，比较开始日期和调整后的当前日期的大小关系
            if (OPERATOR_HOUR.equals(operator)) {
                flag = startDate.isAfter(now.plusHours(timeRange));
            } else if (OPERATOR_MINUTE.equals(operator)) {
                flag = startDate.isAfter(now.plusMinutes(timeRange));
            } else if (OPERATOR_SECOND.equals(operator)) {
                flag = startDate.isAfter(now.plusSeconds(timeRange));
            } else {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "时间格式错误,请检查参数配置[{}]", operator);
                // 时间格式不正确，抛出异常并记录错误日志信息体
                throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("时间格式错误,请检查参数配置[{}]", operator), null);
            }
            // 异常捕获，处理日期时间解析和比较过程中可能出现的任何异常情况
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "时间范围判断失败 [{}]", e.getMessage(), e);
            // 抛出流程异常，处理时间范围比较失败场景下的异常情况
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("时间范围判断失败 [{}]", e.getMessage()), e);
        }
        // 根据时间比较结果更新原子结果类型，成功则设置为成功，否则保持失败状态
        if (flag) {
            atomResult.setRetType(RetType.SUCCESS);
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        // 返回原子结果，包含本次业务逻辑执行的结果状态和相关信息体内容输出单元格内
        return atomResult;
    }
}