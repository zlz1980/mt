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

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 客户姓名脱敏
 * <p>
 * 原子交易参数，单参支持常量和变量
 * ${XXXX}
 * 增强处理参数
 * 无需配置
 * 脱敏规则：
 * - 如果姓名长度超过4个字母（或2个汉字），则保留最后4个字母（或2个汉字），前面全部反显为＊
 * - 否则保留最后2个字母（或1个汉字），其余反显为＊
 *
 * @author 
 * @create 2025/12/17 13:38
 */
@Atom("fbsp.UserNameReset")
public class UserNameResetImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserNameResetImpl.class);
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]");
    private static final int NAME_LENGTH_2 = 2;
    private static final int NAME_LENGTH_4 = 4;


    /**
     * 客户姓名脱敏服务执行方法
     *
     * @param tranContext  交易上下文，用于传递交易过程中所需的上下文信息
     * @param scopeValUnit 作用域值单元，用于存储和传递作用域内的值
     * @param flowUnit     流程单元，包含流程执行所需的参数和配置
     * @return 返回原子结果对象，包含执行结果
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化结果对象
        AtomResult atomResult = new AtomResult();
        // 检查原子交易参数是否为空或未配置
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取流程传输参数
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 检查原子交易参数个数是否为1
        if (atomTranParams.length != PARAM_NUM_ONE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam), null);
        }
        String userName;
        // 检查是否获取到客户姓名
        if (ObjectUtils.isEmpty(tranContext.getCtxVal(atomTranParam))) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到客户姓名,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到客户姓名,请检查参数配置[{}]", atomTranParam), null);
        } else {
            userName = tranContext.getValue(atomTranParam).toString();
        }
        try {
            // 处理姓名脱敏
            String maskedUserName = maskUserName(userName);
            // 将处理后的结果放入上下文
            scopeValUnit.put("userName", maskedUserName);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "客户姓名脱敏异常,异常信息[{}]!", e.getMessage(), e);
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("客户姓名脱敏异常，异常信息[{}]!", e.getMessage()), null);
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        // 设置执行结果为成功
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }


    /**
     * 根据规则对姓名进行脱敏处理
     *
     * @param userName 原始姓名
     * @return 脱敏后的姓名
     */
    private String maskUserName(String userName) {
        int length = userName.length();
        // 判断是否包含中文字符
        if (CHINESE_PATTERN.matcher(userName).find()) {
            // 中文姓名处理
            if (length > NAME_LENGTH_2) {
                // 超过2个汉字，保留最后2个汉字
                return IntStream.range(0, length - NAME_LENGTH_2).mapToObj(i -> "*")
                                .collect(Collectors.joining("")) + userName.substring(length - NAME_LENGTH_2);
            } else {
                // 不超过2个汉字，保留最后1个汉字
                return IntStream.range(0, length - 1).mapToObj(i -> "*")
                                .collect(Collectors.joining("")) + userName.substring(length - 1);
            }
        } else {
            // 英文姓名处理
            if (length > NAME_LENGTH_4) {
                // 超过4个字母，保留最后4个字母
                return IntStream.range(0, length - NAME_LENGTH_4).mapToObj(i -> "*")
                                .collect(Collectors.joining("")) + userName.substring(length - NAME_LENGTH_4);
            } else {
                // 不超过4个字母，保留最后2个字母
                return IntStream.range(0, length - NAME_LENGTH_2).mapToObj(i -> "*")
                                .collect(Collectors.joining("")) + userName.substring(length - NAME_LENGTH_2);
            }
        }
    }

}