package com.n.fbsp.atom.platform.seed.util.bean;

import com.nantian.nbp.flow.engine.service.api.Util;
import com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.JsonUtil;
import com.nantian.nbp.utils.StrUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Map;

/**
 * @author Lianghaizhen
 */
@Util("StringUtils")
public class StringUtilsBean {

    /**
     * 使用零填充字符串至指定宽度
     * <p>
     * 此方法旨在将输入的字符串通过在左侧添加零的方式，扩展到指定的宽度
     * 如果输入字符串的长度已经等于或大于指定宽度，则不会进行任何填充
     *
     * @param width 填充后的目标宽度，必须是非负数
     * @param input 需要进行填充的原始字符串
     * @return 返回填充后的字符串
     */
    public static String padLeftWithZero(int width, String input) {
        return StrUtils.padLeftWithZero(width, input);
    }

    /**
     * 使用指定字符从左端填充字符串至指定宽度
     *
     * @param width  目标字符串的总宽度
     * @param input  需要填充的原始字符串
     * @param ch     用于填充的字符
     * @return       填充后的字符串
     *
     * 此方法通过调用StrUtils类中的padLeftWithChar方法实现字符串的左填充操作
     * 它主要用于在字符串左侧添加特定字符，直到达到指定的宽度
     */
    public static String padLeftWithChar(int width, String input, char ch) {
        return StrUtils.padLeftWithChar(width, input, ch);
    }

    /**
     * 使用零填充字符串至指定宽度
     *
     * 此方法旨在通过在字符串的右侧添加零来填充字符串，直到达到指定的宽度
     * 它实际上是一个代理方法，调用了StrUtils类中的同名方法来实现功能
     *
     * @param width 填充后的字符串目标宽度如果输入字符串的长度已经大于或等于这个宽度，则不会进行填充
     * @param input 需要被填充的原始字符串
     * @return 返回填充后的字符串如果输入字符串已经达到或超过指定宽度，则返回原始字符串
     */
    public static String padRightWithZero(int width, String input) {
        return StrUtils.padRightWithZero(width, input);
    }

    /**
     * 使用指定字符从右侧填充字符串至指定宽度
     *
     * 此方法委托给StrUtils类中的同名方法实现字符串的填充操作，以避免代码重复并促进代码复用
     * 它的存在简化了对外部调用的接口，隐藏了内部实现细节
     *
     * @param width 填充后的字符串宽度如果输入字符串的长度大于或等于此宽度，则不进行填充
     * @param input 需要填充的原始字符串
     * @param ch 用于填充的字符
     * @return 填充后的字符串如果输入字符串已经达到或超过指定宽度，则返回原始字符串
     */
    public static String padRightWithChar(int width, String input, char ch) {
        return StrUtils.padRightWithChar(width, input, ch);
    }

    /**
     * 使用指定字符按GBK编码填充字符串至指定宽度
     *
     * 本方法通过调用StrUtils类中的同名静态方法实现字符串的右填充操作，目的是为了确保字符串在按照GBK编码进行处理时，
     * 能够达到预期的显示宽度。由于不同的字符在GBK编码下的字节长度不同，因此直接使用字符数量来控制宽度可能不够准确，
     * 需要通过本方法来实现精确的宽度控制。
     *
     * @param width 目标宽度，表示字符串在GBK编码下的预期字节长度
     * @param input 需要进行填充操作的原始字符串
     * @param ch 用于填充的字符，当原始字符串的GBK编码字节长度不足时，将使用此字符补充至目标宽度
     * @return 返回填充后的字符串，如果原始字符串的GBK编码字节长度已经达到或超过目标宽度，则不进行填充，原样返回
     */
    public static String padRightWithCharByGbk(int width, String input, char ch) {
        return StrUtils.padRightWithCharByGbk(width, input, ch);
    }

    /**
     * 使用指定字符在GBK编码下左填充字符串至指定宽度
     *
     * 此方法用于在GBK编码下，将给定的字符串通过在左侧填充指定的字符，扩展到指定的宽度
     * 这在需要对齐文本或填充字段以满足特定的格式要求时特别有用
     *
     * @param width 目标字符串的宽度（以GBK编码字节为单位）
     * @param input 需要填充的原始字符串
     * @param ch 用于填充的字符
     * @return 填充后的字符串如果输入字符串在GBK编码下的字节长度已经达到或超过指定宽度，则返回原始字符串
     */
    public static String padLeftWithCharByGbk(int width, String input, char ch) {
        return StrUtils.padLeftWithCharByGbk(width, input, ch);
    }

    public static String toString(Object obj) {
        return StrUtils.toString(obj);
    }

    /**
     * 根据提供的参数截取字符串。
     * 支持通过递归调用自身来处理不同数量的参数。
     *
     * @param origString 原始字符串。
     * @param args       参数，可以是两个integers表示起始和结束index，或者单一index。
     * @return 截取后的字符串，如果输入不合法返回null。
     */
    public static String subString(String origString, Object... args) {
        // 首先，检查原字符串是否为空
        if (ObjectUtils.isEmpty(origString)) {
            return null;
        }
        // 单参数情况
        if (args.length == 1 && args[0] instanceof Integer) {
            return substringWithSingleArg(origString, (Integer) args[0]);
        }
        // 双参数情况
        else if (args.length == 2 && args[0] instanceof Integer && args[1] instanceof Integer) {
            return substringWithDoubleArg(origString, (Integer) args[0], (Integer) args[1]);
        } else {
            throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("字符串截取方法参数格式不合法，数据源字符串[{}]", origString));
        }
    }

    /**
     * 从原始字符串中提取子字符串。
     * 如果起始索引超出范围，则返回原始字符串表示错误情况。
     * 注意：返回null表示需要调用者注意处理错误情况或空值。
     *
     * @param origString 原始字符串
     * @param startIndex 子字符串的起始位置
     * @return 子字符串或表示错误的null
     */
    private static String substringWithSingleArg(String origString, int startIndex) {
        String result = null;
        // 如果val值为空或者长度为0，则返回其自身
        if (!StringUtils.hasText(origString)) {
            return origString;
        }
        try {
            // 添加判断逻辑，以确保起始和结束索引不会越界，并且确保不截取超过字符串长度的部分。
            if (startIndex >= 0 && startIndex < origString.length()) {
                // 使用安全的逻辑获取更新后的子字符串，避免截取超过字符串长度的部分
                result = origString.substring(startIndex);
            } else {
                // 如果索引越界，则返回null
                throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("字符串截取方法参数错误，数据源字符串[{}]，起始索引[{}]", origString, startIndex));
            }
        } catch (NumberFormatException | NullPointerException e) {
            return result;
        }
        return result;
    }

    /**
     * 从原始字符串中提取子字符串。
     * 如果起始索引超出范围，则返回原始字符串表示错误情况。
     * 注意：返回null表示需要调用者注意处理错误情况或空值。
     *
     * @param origString 原始字符串
     * @param startIndex 子字符串的起始位置
     * @param endIndex   子字符串的结束位置
     * @return 子字符串或表示错误的null
     */
    private static String substringWithDoubleArg(String origString, int startIndex, int endIndex) {
        String result = null;
        // 如果val值为空或者长度为0，则返回其自身
        if (!StringUtils.hasText(origString)) {
            return origString;
        }
        try {
            // 添加判断逻辑，以确保起始和结束索引不会越界，并且确保不截取超过字符串长度的部分。
            if (startIndex >= 0 && startIndex < origString.length() && startIndex < endIndex) {
                // 使用安全的逻辑获取更新后的子字符串，避免截取超过字符串长度的部分
                result = origString.substring(startIndex, Math.min(endIndex, origString.length()));
            } else {
                throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("字符串截取方法参数错误，数据源字符串[{}]，起始索引[{}]，截止索引[{}]", origString, startIndex, endIndex));
            }
        } catch (NumberFormatException | NullPointerException e) {
            return result;
        }
        return result;
    }

    /**
     * 判断给定的字符串是否为空或仅包含空白字符
     *
     * @param str 待检查的字符串
     * @return 如果字符串为空或仅包含空白字符，则返回true；否则返回false
     */
    public static boolean isEmpty(String str) {
        return !StringUtils.hasText(str);
    }

    /**
     * 检查给定的字符串是否包含文本内容.
     * <p>
     * 此方法委托给Spring框架的StringUtils类中的hasText方法来判断字符串是否非空且包含非空白字符.
     * 使用此方法可以确保字符串不仅非null，而且包含至少一个非空白字符，这对于验证输入字符串是否真正含有信息非常有用.
     *
     * @param str 要检查的字符串
     * @return 如果字符串非null且至少包含一个非空白字符，则返回true；否则返回false.
     */
    public static boolean hasText(String str) {
        return StringUtils.hasText(str);
    }


    /**
     * 将对象转换为字符串，如果对象为null则返回空字符串
     * 此方法主要用于简化对象到字符串的转换过程，特别是在处理可能为null的值时
     * 使用StrUtils工具类的toStrDefBlank方法来实现转换，保证了代码的健壮性
     *
     * @param val 需要转换的对象，可以是任何类型
     * @return 转换后的字符串，如果输入为null，则返回空字符串
     */
    public static String toStrDefBlank(Object val) {
        return StrUtils.toStrDefBlank(val);
    }

    /**
     * 将JSON字符串转换为键值对映射
     * 此方法主要用于将JSON格式的字符串转换为Map对象，以便于后续处理
     * 如果输入的JSON字符串为null，则返回null
     *
     * @param json JSON格式的字符串
     * @return 转换后的Map对象，键为String类型，值为Object类型如果输入为null，则返回null
     */
    public Map<String, Object> toJson(String json) {
        return json == null ? null : JsonUtil.strToMap(json);
    }

    /**
     * 将字节数组编码为Base64字符串
     * 如果输入的base64数组为null，则返回null
     *
     * @param val 待编码的字节数组
     * @return 编码后的Base64字符串，如果输入为null或空数组，则返回null
     */
    public static String base64Encode(byte[] val) {
        // 检查输入是否为null或空数组，如果是，则返回null
        if (val == null || val.length == 0) {
            return null;
        }
        // 使用Base64编码器将字节数组编码为字符串
        return Base64.getEncoder().encodeToString(val);
    }

    /**
     * 对给定的Base64编码字符串进行解码操作
     *
     * @param val 待解码的Base64编码字符串如果字符串为空或null，则返回null
     * @return 解码后的字节数组如果输入字符串为空或null，则返回null
     */
    public static byte[] base64Decode(String val) {
        // 检查输入字符串是否为空或仅包含空白字符，如果是，则返回null
        if (!StringUtils.hasText(val)) {
            return null;
        }
        // 使用Base64解码器对输入字符串进行解码，并返回解码后的字节数组
        return Base64.getDecoder().decode(val);
    }
}

