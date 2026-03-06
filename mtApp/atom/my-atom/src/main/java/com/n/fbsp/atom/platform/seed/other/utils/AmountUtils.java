    package com.n.fbsp.atom.platform.seed.other.utils;

import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_DOT_SPILT;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * @author
 * @date 2025/6/23
 * @description TODO
 */
public class AmountUtils {

    public static String getAmountKey(String atomTranParam) {
        // 提取参数中的 ${...} 内容（如将 "${suffix}" 转换为 "suffix"）
        String amountPathStr;
        // 匹配 ${...} 结构
        Pattern pattern = Pattern.compile("\\$\\{(.*?)}");
        Matcher matcher = pattern.matcher(atomTranParam);
        if (matcher.find()) {
            amountPathStr = matcher.group(1);
        } else {
            // 若未找到匹配项，抛出异常
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数缺少${...}格式的内容,请检查参数配置[{}]", atomTranParam));
        }
        // 解析参数：目标键值后缀和待解析金额栏位
        String[] keys = StringUtils.delimitedListToStringArray(amountPathStr, PARAM_DOT_SPILT);
        return keys[keys.length - 1];
    }
}
