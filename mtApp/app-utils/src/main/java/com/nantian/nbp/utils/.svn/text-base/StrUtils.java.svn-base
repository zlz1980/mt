package com.nantian.nbp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Set;

/**
 * 字符串工具类
 *
 * @author JiangTaiSheng
 */
public class StrUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrUtils.class);

    public static final String EMPTY_STR = "";

    private static final Charset GBK = Charset.forName("GBK");

    public static String toStrDefBlank(Object val) {
        return toStr(val, "");
    }

    public static String toStrDefNull(Object val) {
        return toStr(val, null);
    }

    public static String toStr(Object val, String defStr) {
        return Objects.toString(val, defStr);
    }

    public static String trim(String val) {
        if (Objects.nonNull(val)) {
            return val.trim();
        }
        return EMPTY_STR;
    }

    public static Set<String> commaDelimitedListToSet(String str){
        return StringUtils.commaDelimitedListToSet(str);
    }

    public static boolean hasText(String str) {
        return StringUtils.hasText(str);
    }
    /**
     * 将任意对象转换为字符串
     *
     * @param obj 任意对象
     * @return 转换后的字符串表示
     */
    public static String toString(Object obj) {
        if (ObjectUtils.isEmpty(obj)) {
            return "null";
        }
        if (obj instanceof String) {
            // 如果是字符串，直接返回
            return (String) obj;
        }
        if (obj instanceof Number || obj instanceof Boolean || obj instanceof Character) {
            // 基础类型直接转换为字符串
            return String.valueOf(obj);
        }
        if (obj instanceof Iterable) {
            StringBuilder sb = new StringBuilder("[");
            for (Object item : (Iterable<?>) obj) {
                sb.append(toString(item)).append(", ");
            }
            if (sb.length() > 1) {
                // 去掉最后的逗号和空格
                sb.setLength(sb.length() - 2);
            }
            sb.append("]");
            return sb.toString();
        }
        if (obj.getClass().isArray()) {
            StringBuilder sb = new StringBuilder("[");
            int length = java.lang.reflect.Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                sb.append(toString(java.lang.reflect.Array.get(obj, i))).append(", ");
            }
            if (sb.length() > 1) {
                sb.setLength(sb.length() - 2);
            }
            sb.append("]");
            return sb.toString();
        }
        if (obj instanceof java.util.Map) {
            StringBuilder sb = new StringBuilder("{");
            for (Object entry : ((java.util.Map<?, ?>) obj).entrySet()) {
                java.util.Map.Entry<?, ?> mapEntry = (java.util.Map.Entry<?, ?>) entry;
                sb.append(toString(mapEntry.getKey())).append("=").append(toString(mapEntry.getValue())).append(", ");
            }
            if (sb.length() > 1) {
                sb.setLength(sb.length() - 2);
            }
            sb.append("}");
            return sb.toString();
        }
        // 使用 Jackson 进行复杂对象的序列化
        try {
            return JsonUtil.objToString(obj);
        } catch (Exception e) {
            LOGGER.error("JsonUtil.objToString exception", e);
            // 如果序列化失败，回退到调用 obj.toString()
            return obj.toString();
        }
    }

    /**
     * 使用零填充字符串至指定宽度
     * <p>
     * 此方法旨在通过在字符串左侧添加零来达到指定的宽度，如果输入字符串的长度已经大于或等于指定宽度，
     * 则不会进行任何填充直接返回原字符串
     *
     * @param width 目标宽度，即最终字符串的最小长度
     * @param input 需要进行填充的字符串
     * @return 返回填充后的字符串
     */
    public static String padLeftWithZero(int width, String input) {
        return padLeftWithChar(width, input, '0');
    }

    /**
     * 左侧填充字符串，使其达到指定长度。
     * 此方法用于将输入的字符串在左侧用char填充，直到达到指定的宽度
     * 如果输入字符串的长度已经大于或等于指定宽度，则直接返回原字符串
     *
     * @param input 要进行填充的原始字符串
     * @param width 填充后的字符串应达到的总宽度
     * @param ch    填充的字符
     * @return 填充后的字符串如果输入字符串长度大于等于宽度，则返回原字符串
     */
    public static String padLeftWithChar(int width, String input, char ch) {
        String inputStr = toStrDefBlank(input);
        // 检查输入字符串长度是否已经满足或超过指定宽度
        if (inputStr.length() >= width) {
            return inputStr;
        }
        StringBuilder sb = new StringBuilder();
        // 计算需要填充的零的数量，并将其添加到StringBuilder中
        for (int i = inputStr.length(); i < width; i++) {
            sb.append(ch);
        }
        // 将原始字符串追加到StringBuilder中
        sb.append(inputStr);
        // 返回填充后的字符串
        return sb.toString();
    }

    /**
     * 使用零填充字符串至指定宽度
     * <p>
     * 此方法旨在通过在字符串右侧添加零来扩展字符串至指定宽度
     * 它是一个便捷方法，专门用于使用字符 '0' 进行填充
     *
     * @param width 目标字符串宽度
     * @param input 需要填充的原始字符串
     * @return 填充后的字符串
     */
    public static String padRightWithZero(int width, String input) {
        return padRightWithChar(width, input, '0');
    }

    /**
     * 右侧填充字符串，使其达到指定长度。
     * 如果原字符串长度已超过或等于指定长度，则返回原字符串。
     *
     * @param input 要进行填充的原始字符串
     * @param width 填充后的字符串应达到的总宽度
     * @param ch    填充的字符
     * @return 填充后的字符串如果输入字符串长度大于等于宽度，则返回原字符串
     */
    public static String padRightWithChar(int width, String input, char ch) {
        String inputStr = toStrDefBlank(input);
        // 检查输入字符串长度是否已经满足或超过指定宽度
        if (inputStr.length() >= width) {
            return inputStr;
        }
        int paddingLength = width - input.length();
        StringBuilder sb = new StringBuilder(inputStr);
        // 计算需要填充的char的数量，并将其添加到StringBuilder中
        for (int i = 0; i < paddingLength; i++) {
            sb.append(ch);
        }
        // 返回填充后的字符串
        return sb.toString();
    }


    /**
     * 使用指定字符按GBK编码长度右对齐字符串
     * 如果输入字符串的GBK编码长度小于指定宽度，则在右侧填充指定字符
     * 如果输入字符串的GBK编码长度大于或等于指定宽度，则截取到指定宽度
     *
     * @param width 目标宽度
     * @param input 输入字符串
     * @param ch    用于填充的字符
     * @return 对齐后的字符串
     */
    public static String padRightWithCharByGbk(int width, String input, char ch) {
        // 将输入字符串转换为默认的非空字符串
        String inputStr = toStrDefBlank(input);
        // 根据GBK编码长度截取字符串到指定宽度
        String safeStr = truncateByGbkLength(inputStr, width);
        // 获取截取后字符串的GBK编码长度
        int gbkLength = getGbkLength(safeStr);
        // 计算需要填充的字符数量
        int padCount = Math.max(0, width - gbkLength);
        // 生成填充字符串
        String padStr = repeatChar(ch, padCount);
        // 返回对齐后的字符串
        return safeStr + padStr;
    }

    /**
     * 使用指定字符从左到右填充字符串，以满足指定的GBK宽度
     * 该方法首先将输入对象转换为字符串，然后根据GBK编码截取到合适的长度
     * 如果截取后的字符串长度小于指定宽度，则在左侧用指定字符填充
     *
     * @param width 需要达到的GBK宽度
     * @param input 输入的字符串
     * @param ch    用于填充的字符
     * @return 填充后的字符串
     */
    public static String padLeftWithCharByGbk(int width, String input, char ch) {
        // 将输入对象转换为字符串，如果为空则使用默认值
        String inputStr = toStrDefBlank(input);
        // 根据GBK编码截取到合适的长度
        String safeStr = truncateByGbkLength(inputStr, width);
        // 计算截取后的字符串的GBK长度
        int gbkLength = getGbkLength(safeStr);
        // 计算需要填充的字符数量
        int padCount = Math.max(0, width - gbkLength);
        // 生成填充字符串
        String padStr = repeatChar(ch, padCount);
        // 返回填充后的字符串
        return padStr + safeStr;
    }

    /**
     * 计算字符串在GBK编码下的字节长度
     * <p>
     * 此方法用于获取给定字符串在GBK编码下的字节长度
     *
     * @param str 要计算长度的字符串
     * @return 字符串在GBK编码下的字节长度
     */
    private static int getGbkLength(String str) {
        return str.getBytes(GBK).length;
    }

    /**
     * 截断字符串，确保在 GBK 编码下字节长度不超过 maxLength，不截断半个汉字
     *
     * @param str       待截断的字符串
     * @param maxLength 允许的最大字节长度
     * @return 截断后的字符串
     */
    private static String truncateByGbkLength(String str, int maxLength) {
        // 如果字符串为空或最大长度小于等于0，则返回空字符串
        if (!StringUtils.hasText(str)) {
            return "";
        }
        // 将字符串转换为 GBK 编码的字节数组
        byte[] bytes = str.getBytes(GBK);
        // 如果字节数组长度小于等于最大长度，则直接返回原字符串
        if (bytes.length <= maxLength) {
            return str;
        }

        // 使用 StringBuilder 来构建截断后的字符串
        StringBuilder sb = new StringBuilder();
        // 记录当前的字节长度总和
        int total = 0;
        // 遍历字符串中的每个字符
        for (char ch : str.toCharArray()) {
            // 计算当前字符在 GBK 编码下的字节长度
            int len = String.valueOf(ch).getBytes(GBK).length;
            // 如果加上当前字符的字节长度后超过最大长度，则停止截断
            if (total + len > maxLength) {
                break;
            }
            // 将字符追加到 StringBuilder 中
            sb.append(ch);
            // 更新当前的字节长度总和
            total += len;
        }
        // 返回截断后的字符串
        return sb.toString();
    }

    /**
     * 生成一个由指定字符重复组成的字符串
     *
     * @param ch    要重复的字符
     * @param count 字符重复的次数
     * @return 由重复字符组成的字符串
     */
    private static String repeatChar(char ch, int count) {
        // 初始化StringBuilder，容量为重复次数，以优化性能
        StringBuilder sb = new StringBuilder(count);
        // 循环将字符追加到StringBuilder中
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        // 将StringBuilder转换为字符串并返回
        return sb.toString();
    }
}
