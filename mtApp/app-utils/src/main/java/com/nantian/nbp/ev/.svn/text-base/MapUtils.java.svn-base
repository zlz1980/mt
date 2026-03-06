/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.ev;

import com.nantian.nbp.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Map与变量标识符${}的操作
 *
 * @author JiangTaiSheng
 */
public class MapUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapUtils.class);

    private static final String ELVAR_START_FLAG = "${";

    private static final String DOT_REGEX = "\\.";

    private static final String VAL_EL_REGEX = "\\$\\{(.*?)}";

    private static final Pattern VAL_EL_REGEX_PATTERN = Pattern.compile(VAL_EL_REGEX);


    /**
     * 根据$去掉标{}识别变量返回其识符，若没有变量标识符直接返回
     *
     * @param valEl 变量表达式
     * @return String 变量表达式
     */
    public static String spiltVal(String valEl) {
        return spiltVal(valEl, 1);
    }

    /**
     * 根据${}识别变量返回根据group参数
     *
     * @param valEl 变量表达式
     * @param group 返回值方式
     * @return String 变量表达式
     */
    private static String spiltVal(String valEl, int group) {
        Matcher m = VAL_EL_REGEX_PATTERN.matcher(valEl);
        if (m.find()) {
            return m.group(group);
        }
        return valEl;
    }

    /**
     * 替换变量表达式将${}替换为#开头表达式
     *
     * @param valEl 变量表达式
     * @return String 替换后的表达式
     */
    public static String repElVal(String valEl) {
        StringBuilder valElBuilder = new StringBuilder(valEl);
        Matcher m = VAL_EL_REGEX_PATTERN.matcher(valElBuilder);

        while (m.find()) {
            String tmp = m.group(0);
            if (tmp == null) {
                throw new RuntimeException(String.format("valEl[%s] replace err", valEl));
            }
            String[] ss = tmp.substring(2, tmp.length() - 1).split(DOT_REGEX, -1);
            int size = ss.length;
            if (size > 0) {
                StringBuilder res = new StringBuilder(Constants.EL_CTX_FLAG);
                for (int i = 0; i<size;i++) {
                    String s = ss[i];
                    if(s.endsWith("?")){
                        s = s.substring(0, s.length()-1);
                    }
                    res.append(".get('")
                            .append(s)
                            .append("')");
                    if(i != size -1){
                        res.append("?");
                    }
                }
                valElBuilder.replace(m.start(), m.end(), res.toString());
                m = VAL_EL_REGEX_PATTERN.matcher(valElBuilder);

            }
        }
        return valElBuilder.toString();
    }


    /**
     * 按"."的层次为MAP取值
     *
     * @param map 目标Map对象
     * @param key 包含"."的key
     * @return Object
     */
    @SuppressWarnings({"unchecked"})
    public static Object getVal(Map<String, Object> map, String key) {
        String[] keys = key.split(DOT_REGEX, -1);
        Map<String, Object> tmp = map;
        for (int i = 0, size = keys.length; i < size; i++) {
            Object v = tmp.get(keys[i]);
            if (i + 1 != size && v instanceof Map) {
                tmp = (Map<String, Object>) v;
            } else {
                return v;
            }
        }
        return map.get(key);
    }

    /**
     * 按"."的层次为MAP赋值
     *
     * @param map   目标Map对象
     * @param key   包含"."的key
     * @param value 赋值
     */
    @SuppressWarnings({"unchecked"})
    public static void setVal(Map<String, Object> map, String key, Object value) {
        if (Objects.isNull(key)) {
            return;
        }
        String[] keys = key.split(DOT_REGEX, -1);
        Map<String, Object> tmp = map;
        Map<String, Object> next;
        for (int i = 0, size = keys.length; i < size; i++) {
            Object m = tmp.get(keys[i]);
            if (m instanceof List) {
                throw new RuntimeException(String.format("赋值操作不支持变量路径中间出现List类型, 变量路径[%s]", key));
            } else if (i == size - 1) {
                tmp.put(keys[i], value);
                return;
            } else if (m == null) {
                next = new LinkedHashMap<>(16);
                tmp.put(keys[i], next);
                tmp = next;
            } else if (m instanceof Map) {
                tmp = (Map<String, Object>) m;
            } else {
                throw new RuntimeException(String.format("赋值操作不支持变量路径中间出现非Map类型, 变量路径[%s]", key));
            }
        }
        map.put(key, value);
    }

    public static Object getValue(Map<String, Object> map, String valEl) {
        return getValue(map, valEl, 1);
    }

    /**
     * 取值，如果为变量表达式，取变量，不存在，返回原值
     *
     * @param map   参数map
     * @param valEl 参数值表达式，可为常量
     * @param group 取匹配的第几个表达式
     * @return Object 返回对应值
     */
    private static Object getValue(Map<String, Object> map, String valEl, int group) {
        Matcher m = VAL_EL_REGEX_PATTERN.matcher(valEl);
        String key;
        if (m.find()) {
            key = m.group(group);
            return getVal(map, key);
        }
        return valEl;
    }

    public static boolean checkEl(String valEl) {
        Matcher m = VAL_EL_REGEX_PATTERN.matcher(valEl);
        return m.find();
    }

    /**
     * 获取表达式中的变量集合
     * @param valEl 表达式
     * @return 变量集合
     */
    public static List<String> getElKeys(String valEl){
        LinkedList<String> list = new LinkedList<>();
        Matcher m = VAL_EL_REGEX_PATTERN.matcher(valEl);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }
}
