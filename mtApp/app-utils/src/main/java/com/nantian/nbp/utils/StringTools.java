package com.nantian.nbp.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;


public class StringTools {

    private final static Logger log = LogManager.getLogger(StringTools.class);

    public static String convertUtf8ToGb18030(String utf8Str) {
        if (utf8Str == null) {
            return null;
        }
        // 先按 UTF-8 取字节，再用 GB18030 重新组装
        try {
            return new String(utf8Str.getBytes(StandardCharsets.UTF_8), "GB18030");
        } catch (UnsupportedEncodingException e) {
            log.error("convert to gbk error");
            return null;
        }
    }


    public static String convertGbkToUtf8(byte[] gbBs) {

        if (gbBs == null) {
            return null;
        }
        /* 关键步骤：
         * 1. 先把 gbStr 按 GB18030 取字节（还原成真正的字节序列）
         * 2. 再按 UTF_8 重新组装为字符串
         */
//        byte[] gbBytes = gbStr.getBytes(Charset.forName("GB18030"));
        String str = "";
        try {
            str = new String(gbBs, "GB18030");
        } catch (UnsupportedEncodingException e) {
            log.error("[{}] not gbk encode", HexTools.encodeHexStr(gbBs));
            return null;
        }

        return new String(str.getBytes(), StandardCharsets.UTF_8);
    }


    //取固定长度字符串，不足补pad,超长，则截断
    public static String padding(String str, int len, char padding, boolean isRight) {
        if (str == null) {
            str = "";
        }
        if (str.length() >= len) {
            return str.substring(0, len);
        }
        StringBuilder out = new StringBuilder(str);
        for (int i = 0; i < len - str.length(); i++) {
            if (isRight) {
                out.append(padding);
            } else {
                out.insert(0, padding);
            }
        }

        return out.toString();
    }

    public static byte[] paddingBytes(byte[] bs, int len, char padding, boolean isRight) {
        byte[] resultBs = new byte[len];
        if (bs.length >= len) {
            System.arraycopy(bs, 0, resultBs, 0, len);
        } else {
            //先用padding填充所有空间
            Arrays.fill(resultBs, (byte) padding);
            if (isRight) {
                System.arraycopy(bs, 0, resultBs, len - bs.length, bs.length);
            } else {
                System.arraycopy(bs, 0, resultBs, 0, bs.length);
            }

        }

        return resultBs;
    }

    public static String getPrintableVal(byte[] bs) {
        boolean isPrintable = true;
        if (bs == null) {
            return "null";
        }
        for (int i = 0; i < bs.length; i++) {
            isPrintable &= !Character.isISOControl(bs[i]);
//            isPrintable &= !(Character.isLetterOrDigit(bs[i])||Character.isSpaceChar(bs[i]));
        }
        if (isPrintable) {
            return new String(bs);
        } else {
            return "0x" + HexTools.encodeHexStr(bs).toUpperCase();
        }
    }

    public static String hexDump(byte[] data) {
        StringBuilder dump = new StringBuilder();
        int bytesPerLine = 16; // 每行显示的字节数
        int length = data.length;

        for (int i = 0; i < length; i += bytesPerLine) {
            // 当前行的起始索引和结束索引
            int start = i;
            int end = Math.min(i + bytesPerLine, length);

            // 十六进制部分
            StringBuilder hex = new StringBuilder();
            // ASCII部分
            StringBuilder ascii = new StringBuilder();

            for (int j = start; j < end; j++) {
                // 将字节转换为两位十六进制字符串
                hex.append(String.format("%02X ", data[j]));
                // 将字节转换为ASCII字符，非可打印字符用`.`代替
                ascii.append((data[j] >= 32 && data[j] <= 126) ? (char) data[j] : '.');
            }

            // 补齐十六进制部分的空格
            while (hex.length() < bytesPerLine * 3) {
                hex.append("   ");
            }

            // 构建最终的dump字符串
            dump.append(String.format("%08X: %s  %s\n", start, hex.toString(), ascii.toString()));
        }

        return dump.toString();
    }


    /**
     * 判断字符串是 an :字母和数字
     *
     * @param str
     * @return
     */
    public static boolean isANString(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断字符串是 n :数字
     *
     * @param str
     * @return
     */
    public static boolean isNumString(String str) {

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 解码字节码
     *
     * @param data    字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * {@link CharSequence} 转为字符串，null安全
     *
     * @param cs {@link CharSequence}
     * @return 字符串
     */
    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    public static StringBuilder builder(int capacity) {
        return new StringBuilder(capacity);
    }

    /**
     * 去除字符串中指定的多个字符，如有多个则全部去除
     *
     * @param str   字符串
     * @param chars 字符列表
     * @return 去除后的字符
     * @since 4.2.2
     */
    public static String removeAll(CharSequence str, char... chars) {
        if (null == str || chars.length == 0) {
            return str(str);
        }
        final int len = str.length();
        if (0 == len) {
            return str(str);
        }
        final StringBuilder builder = builder(len);
        char c;
        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (!arrayContains(chars, c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * 数组中是否包含元素
     *
     * @param array 数组
     * @param value 被检查的元素
     * @return 是否包含
     * @since 3.0.7
     */
    public static boolean arrayContains(char[] array, char value) {
        return arrayIndexOf(array, value) > -1;
    }

    public static int arrayIndexOf(char[] array, char value) {
        if (null != array) {
            for (int i = 0; i < array.length; i++) {
                if (value == array[i]) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String base64Encode(byte[] input) {
        if (input == null || input.length == 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(input);
    }

    public static byte[] base64Decode(String input) {
        if (StringTools.isEmpty(input)) {
            return null;
        }
        return Base64.getDecoder().decode(input);
    }

    /**
     * 输出json字符串，
     * @param data 数据
     * @param ifPretty 是否需要格式化输出
     * @return
     */
    public static String dumpMap(Map data,boolean ifPretty) {

        ObjectWriter objectWriter;
        if (ifPretty) {
            objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        }else{
            objectWriter = new ObjectMapper().writer();
        }

        try {
            return objectWriter.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
