package com.nantian.nbp.utils.trim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * @author Liang Haizhen
 * @date 2025/6/26
 * @description 通用截断方法 POJO内部调用
 */
public class TrimSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrimSupport.class);

    /**
     * 针对当前对象，进行set值设置对字段进行截断
     */
    public void setField(String fieldName, String value) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            AutoTrimLength annotation = field.getAnnotation(AutoTrimLength.class);
            field.setAccessible(true);
            if ((!ObjectUtils.isEmpty(annotation)) && StringUtils.hasText(value) && value.length() > annotation.max()) {
                String trimmed = value.substring(0, annotation.max());
                LOGGER.warn("字段 [{}] 超长,截断 [{}] -> [{}]", fieldName, value, trimmed);
                field.set(this, trimmed);
            } else {
                field.set(this, value);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("字段 [" + fieldName + "] :[" + value + "]超长,截断时发生异常,异常信息[{" + e.getMessage() + "}]");
        }
    }
}
