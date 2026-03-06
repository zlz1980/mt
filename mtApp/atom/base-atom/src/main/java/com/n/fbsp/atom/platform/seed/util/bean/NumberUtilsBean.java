package com.n.fbsp.atom.platform.seed.util.bean;

import com.nantian.nbp.flow.engine.service.api.Util;
import com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Lianghaizhen
 */
@Util("NumberUtils")
public class NumberUtilsBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(NumberUtilsBean.class);

    /**
     * 比较两个金额的大小
     *
     * @param amount1 第一个比较的金额，可以是String、Integer、Double、BigDecimal等表示数值的类型
     * @param amount2 第二个比较的金额，可以是String、Integer、Double、BigDecimal等表示数值的类型
     * @return 返回-1、0或1，分别表示amount1小于、等于或大于amount2
     *
     * @throws FlowException 如果任一输入为null或空字符串，则抛出FlowException异常
     */
    public static int amountCompare(Object amount1, Object amount2) {
        // 检查原始值是否为空并抛出异常
        if (ObjectUtils.isEmpty(amount1) || ObjectUtils.isEmpty(amount2)) {
            throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("amountCompare参数错误,数据源值为空"));
        }
        //判断amount1和amount2的类型，转换为big decimal之后进行大小比较
        BigDecimal bd1 = toBigDecimal(amount1);
        BigDecimal bd2 = toBigDecimal(amount2);
        //对bd1 和 bd2进行比较 如果bd1大于bd2返回1，bd1等于bd2返回0，bd1小于bd2返回-1
        return bd1.compareTo(bd2);
    }

    /**
     * 对传入的一系列值进行加减法操作
     * 该方法接受可变数量的参数，可以是String、Integer、Double、BigDecimal等表示数值的对象
     * 如果任何一个参数为null或空字符串，将抛出FlowException异常
     *
     * @param values 可变数量的参数
     * @return 返回所有参数相加的结果，如果参数中有null或空字符串，则返回null
     *
     * @throws FlowException 如果任何一个参数为null或空字符串，则抛出此异常
     */
    public static BigDecimal amountAddSub(Object... values) {
        BigDecimal result = BigDecimal.ZERO;
        for (Object val : values) {
            // 检查原始值是否为空并抛出异常
            if (ObjectUtils.isEmpty(val)) {
                throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("amountAddSub参数错误,数据源值为空"));
            }
            BigDecimal bd = toBigDecimal(val);
            result = result.add(bd);
        }
        //对result处理，保留两位小数
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 将给定对象转换为BigDecimal并返回其负值。如果对象为null，则返回BigDecimal.ZERO。
     *
     * @param val 需要转换并取负值的对象，应能转换为有效的数值字符串
     * @return 输入对象的负值，若输入为null则返回0
     */
    public static BigDecimal negate(Object val) {
        BigDecimal bdValue = toBigDecimal(val);
        return bdValue.negate();
    }


    /**
     * 执行乘法或除法操作
     * 本方法根据传入的基础值和因子，执行乘法或除法操作
     * 这种设计可以用于处理金额等需要根据因子调整的数值
     *
     * @param baseValue 基础值，可以是String、Integer、Double、BigDecimal等表示数值的对象
     * @param factor    因子 扩大为原来的1.15倍 缩小为原来的0.5
     * @return 计算后的BigDecimal结果
     *
     * @throws FlowException 如果基础值或因子为空，或因子为0，则抛出FlowException异常
     */
    public static BigDecimal amountMulDiv(Object baseValue, Object factor) {
        // 检查输入参数是否为空
        if (ObjectUtils.isEmpty(baseValue) || ObjectUtils.isEmpty(factor)) {
            throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("amountMulDiv参数错误,数据源值为空"));
        }
        // 将输入参数转换为BigDecimal类型
        BigDecimal base = toBigDecimal(baseValue);
        BigDecimal fac = toBigDecimal(factor);
        // 检查因子是否为0
        if (fac.compareTo(BigDecimal.ZERO) == 0) {
            throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("amountMulDiv参数错误,参数[{}]不能为0", fac));
        }
        BigDecimal result = base.multiply(fac);
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 将给定的值转换为BigDecimal对象
     *
     * @param value 要转换的值，可以是null，BigDecimal，Number或String类型
     * @return 转换后的BigDecimal对象，如果输入为null或无法转换，则返回null
     * <p>
     * 此方法处理各种类型的输入值，将其转换为BigDecimal对象以便进行精确的数值计算
     * 它首先检查输入值的类型，然后根据类型执行相应的转换逻辑
     * 对于Number类型，为了避免精度损失，先将其转换为String，再构造BigDecimal对象
     * 对于String类型，尝试直接构造BigDecimal对象，如果失败则抛出异常
     * 如果输入值不是预期类型，方法会尝试将其toString方法的结果转换为BigDecimal
     */
    private static BigDecimal toBigDecimal(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            LOGGER.error("数据源值为空,无法转换为BigDecimal类型");
            throw new FlowException(ErrorCodeEnum.T_C0003094.getCode(), ErrorCodeEnum.T_C0003094.getCodeMsg("数据源值为空,无法转换为BigDecimal类型"));
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            // 避免Double、Float精度问题，优先转String再构造
            return new BigDecimal(value.toString());
        } else if (value instanceof String) {
            String str = ((String) value).trim();
            try {
                return new BigDecimal(str);
            } catch (NumberFormatException e) {
                LOGGER.error("String类型数据[{}] 转换为 BigDecimal发生异常,异常信息[{}]", str, e.getMessage(), e);
                throw new FlowException(ErrorCodeEnum.T_C0003094.getCode(), ErrorCodeEnum.T_C0003094.getCodeMsg("String类型数据[{}] 转换为 BigDecimal发生异常,异常信息[{}]", str, e.getMessage(), e));
            }
        } else {
            LOGGER.error("数据[{}]类型不合法,无法转换为BigDecimal类型", value);
            throw new FlowException(ErrorCodeEnum.T_C0003094.getCode(), ErrorCodeEnum.T_C0003094.getCodeMsg("数据[{}]类型不合法,无法转换为BigDecimal类型", value));
        }
    }

    /**
     * 添加校验方法，仅处理数字和小数点
     */
    private static boolean isValid(String value) {
        // 正则表达式匹配数字和小数点
        return value.matches("\\d+(\\.\\d+)?");
    }

    /**
     * 将原始值根据给定的单位转换因子进行数值转换。
     *
     * @param originalValue 原始值，支持类型为BigDecimal或String，且必须为有效数字格式。
     * @param unitFactor    单位转换因子，必须为有效数字格式的字符串。
     * @return 转换后的数值字符串，去除末尾无效零。
     *
     * @throws IllegalArgumentException 当输入参数无效或原始值类型不支持时抛出。
     */
    public static String rateConvert(Object originalValue, String unitFactor) {
        // 校验输入参数有效性
        if (!isValid(originalValue.toString()) || !isValid(unitFactor)) {
            LOGGER.error("原数据和单位转换参数输入错误,仅支持数字和.");
            throw new IllegalArgumentException("原数据和单位转换参数输入错误,仅支持数字和.");
        }
        String result;
        // 处理BigDecimal类型原始值
        if (originalValue instanceof BigDecimal) {
            double val = ((Number) originalValue).doubleValue();
            BigDecimal bd = BigDecimal.valueOf(val).multiply(new BigDecimal(unitFactor));
            result = bd.stripTrailingZeros().toPlainString();
        } else if (originalValue instanceof String) {
            // 处理String类型原始值
            String valStr = originalValue.toString();
            BigDecimal val = new BigDecimal(valStr).multiply(new BigDecimal(unitFactor));
            result = val.stripTrailingZeros().toPlainString();
        } else {
            // 处理不支持的原始值类型
            LOGGER.error("原数据类型错误,仅支持BigDecimal和String");
            throw new IllegalArgumentException("原数据类型错误,仅支持BigDecimal和String");
        }
        return result;
    }

    /**
     * 将对象转换为整数类型
     *
     * @param originalValue 待转换的原始值对象
     * @return 转换后的整数值
     */
    public static int toInt(Object originalValue) {
        // 检查原始值是否为空并抛出异常
        if (ObjectUtils.isEmpty(originalValue)) {
            throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("toInt转换参数错误，数据源值为空"));
        }
        int result;
        // 执行字符串转换和异常处理
        try {
            result = Integer.parseInt(originalValue.toString().trim());
        } catch (Exception e) {
            throw new FlowException(ErrorCodeEnum.T_X0005091.getCode(), ErrorCodeEnum.T_X0005091.getCodeMsg("toInt转换参数错误,异常原因[{}]"), e.getMessage(), e);
        }
        return result;
    }

}


