/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 *
 */
package org.jts.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于jackson的单利解析JSON
 * @author JiangTaiSheng
 */
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 忽略未知属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略空值
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 将对象转换为JSON字符串
     * @param value 目标对象
     * @return JSON字符串
     */
    public static String objToString(Object value){
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("objToString exception",e);
        }
    }

    /**
     * 将JSON字符串转换为Map
     * @param value JSON字符串
     * @return Map<String,Object>对象
     */
    public static Map<String,Object> strToMap(String value){
        return strToObj(value, HashMap.class);
    }

    /**
     * 将JSON字符串转换为对象
     * @param value JSON字符串
     * @param clz 转换对象类型
     * @return 转换对象
     */
    public static <T> T strToObj(String value,Class<T> clz){
        try {
            return OBJECT_MAPPER.readValue(value, clz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("strToObj exception",e);
        }
    }

    /**
     * 将对象转换为Map
     * @param obj 对象
     * @return 转换为Map<String, Object>
     */
    public static Map<String, Object> beanToMap(Object obj){
        return OBJECT_MAPPER.convertValue(obj,new TypeReference<Map<String, Object>>(){});
    }

    /**
     * 将Map转换为对象
     * @param obj Map对象
     * @param tTypeReference 目标对象引用
     * @return 目标对象
     */
    public static <T> T mapToBean(Map<String,Object> obj,TypeReference<T> tTypeReference){
        return OBJECT_MAPPER.convertValue(obj,tTypeReference);
    }

    /**
     * 深度克隆对象
     * @param obj 原始对象/集合
     * @return 深度克隆后的列表
     */
    public static <T> T deepCloneObj(T obj, TypeReference<T> tTypeReference) {
        if (obj == null) {
            throw new IllegalArgumentException("deepCloneObj param must not be null");
        }
        try {
            String json = OBJECT_MAPPER.writeValueAsString(obj);
            return OBJECT_MAPPER.readValue(json, tTypeReference);
        } catch (Exception e) {
            throw new RuntimeException("deepCloneObj exception",e);
        }
    }
}
