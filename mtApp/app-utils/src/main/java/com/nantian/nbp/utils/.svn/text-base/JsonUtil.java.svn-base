/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Map;

/**
 * 基于jackson的单利解析JSON
 * @author JiangTaiSheng
 */
public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 格式化输出
        MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
        // 忽略那些未知的、不在目标类的getter方法中的属性
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);

        // null字段不输出JSON
        // MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    public static String objToString(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> strToMap(String value) {
        return strToObj(value, Map.class);
    }

    public static <T> T strToObj(String value, Class<T> clz) {
        try {
            return MAPPER.readValue(value, clz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String writeValueAsString(Object value) {
        return objToString(value);
    }

    public static ObjectMapper getMapperInstant() {
        return MAPPER;
    }

    public static Object deepCloneToMap(Object value){
        return strToObj(objToString(value),Object.class);
    }
}
