package com.nantian.nbp.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * @author w46838
 */
public class DeepCloneUtils {

    private static final ObjectMapper MAPPER = JsonMapper.builder()
        /* 字段可见性配置：设置所有字段（包括私有字段）都参与序列化和反序列化，无需getter/setter。*/
        .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        /* 反序列化配置：忽略JSON中存在但Java对象中不存在的属性（默认会抛出异常）。*/
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        /* 反序列化配置：将JSON中的浮点数反序列化为BigDecimal（避免double精度问题）。*/
        .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
        /* 多态类型处理：启用默认类型信息，支持反序列化多态类型（非final类或接口的实现类）。@class*/
        .activateDefaultTyping(
            JsonMapper.builder().build().getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL
        )
        .build();

    /**
     * 深度克隆对象
     * @param obj 被克隆对象
     * @return 克隆后的对象
     */
    public static Object deepClone(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return MAPPER.readValue(MAPPER.writeValueAsString(obj), Object.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
