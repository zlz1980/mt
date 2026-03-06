package com.nantian.nbp.flow.engine.service.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * @author Lianghaizhen
 */
public class JsonSchemaUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSchemaUtils.class);
    private static final String REQUIRED = "required";
    private static final String PROPERTIES = "properties";
    private static final String OBJECT = "object";
    private static final String TYPE = "type";

    /**
     * 校验给定的JSON数据中是否包含了所有必填字段
     *
     * @param schema     JSON模式，用于描述预期的字段和类型信息
     * @param data       待验证的JSON数据
     * @param parentPath 当前节点的父路径，用于标识当前字段的完整路径
     * @param isEnable   是否启用严格模式（是否抛出异常）
     */
    public static void validate(JsonNode schema, JsonNode data, String parentPath, boolean isEnable) {
        // 1. 获取当前层的 "required" 字段
        if (schema.has(REQUIRED) && schema.get(REQUIRED).isArray()) {
            for (JsonNode requiredField : schema.get(REQUIRED)) {
                String fieldName = requiredField.asText();
                String currentPath = parentPath.isEmpty() ? fieldName : parentPath + "." + fieldName;
                // 检查数据是否存在这个字段
                if (!data.has(fieldName)) {
                    LOGGER.debug("必输字段未上送: " + currentPath);
                    if (isEnable) {
                        throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("必输字段未上送: " + currentPath));
                    }
                }
            }
        }
        // 2. 递归检查 "properties" 中的字段
        if (schema.has(PROPERTIES)) {
            Iterator<String> fieldNames = schema.get(PROPERTIES).fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode subSchema = schema.get(PROPERTIES).get(fieldName);
                String currentPath = parentPath.isEmpty() ? fieldName : parentPath + "." + fieldName;
                // 如果数据中包含该字段，递归检查子字段
                if (data.has(fieldName)) {
                    JsonNode subData = data.get(fieldName);
                    //获取当前层的 "type" 字段
                    if (subSchema.has(TYPE)) {
                        String expectedType = subSchema.get(TYPE).asText();
                        if (!isTypeMatching(subData, expectedType)) {
                            LOGGER.debug("数据类型不匹配,预期类型:[{}],当前类型:[{}] ", expectedType, getType(subData));
                            if (isEnable) {
                                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("预期类型:[{}],"
                                        + "当前类型:[{}] ", expectedType, getType(subData)));
                            }
                        }
                    }
                    // 如果子字段的类型是对象，则继续递归校验
                    if (subSchema.has(TYPE) && subSchema.get(TYPE).asText().equals(OBJECT)) {
                        validate(subSchema, subData, currentPath, isEnable);
                    }
                }
            }
        }
    }

    private static boolean isTypeMatching(JsonNode value, String expectedType) {
        switch (expectedType) {
            case "string":
                return value.isTextual();
            case "object":
                return value.isObject();
            case "number":
                return value.isNumber();
            case "boolean":
                return value.isBoolean();
            case "array":
                return value.isArray();
            case "integer":
                return value.isInt();
            default:
                return false;
        }
    }

    private static String getType(JsonNode value) {
        if (value.isTextual()) {
            return "string";
        } else if (value.isObject()) {
            return "object";
        } else if (value.isNumber()) {
            return "number";
        } else if (value.isBoolean()) {
            return "boolean";
        } else if (value.isArray()) {
            return "array";
        } else if (value.isInt()) {
            return "integer";
        } else {
            return "unknown";
        }
    }
}