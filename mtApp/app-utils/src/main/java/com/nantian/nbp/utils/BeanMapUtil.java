package com.nantian.nbp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

public class BeanMapUtil {

    public static <T> T mapToBean(Map<String, Object> map, T instance) {
        try {
            // 先转换成 JSON
            String json = JsonUtil.writeValueAsString(map);
            // 只更新字段，不清空已有值
            return JsonUtil.getMapperInstant().readerForUpdating(instance).readValue(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> beanToMap(Object bean) {
        BeanMap beanMap = BeanMap.create(bean);
        Map<String, Object> result = new HashMap<>();
        for (Object key : beanMap.keySet()) {
            if (beanMap.get(key) != null) {
                // 手动跳过 null 值
                result.put(key.toString(), beanMap.get(key));
            }
        }
        return result;
    }
}
