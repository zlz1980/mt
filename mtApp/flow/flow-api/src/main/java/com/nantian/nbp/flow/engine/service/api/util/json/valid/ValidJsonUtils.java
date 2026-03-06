package com.nantian.nbp.flow.engine.service.api.util.json.valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ListReportProvider;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author Administrator
 */
public class ValidJsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidJsonUtils.class);

    private static final String JSON_SCHEMA_BLOCK_ON = "1";

    private static final JsonSchemaFactory FACTORY = JsonSchemaFactory.byDefault()
            .thaw()
            .setReportProvider(new ListReportProvider(LogLevel.ERROR, LogLevel.FATAL))
            .freeze();

    /**
     * 通过Jsonschema校验json体是否合法
     * @param req json体
     * @param schemaJson json校验体
     * @throws IOException IO异常
     */
    public static void validJsonSchema(byte[] req, String schemaJson, String jsonStatus)
            throws IOException {
        ObjectMapper mapper = JsonUtil.getMapperInstant();
        JsonNode valNode = mapper.readTree(req);
        JsonNode schemaNode;
        try {
            schemaNode = mapper.readTree(schemaJson);
        } catch (JsonProcessingException e) {
            throw new FlowException(ErrorCodeEnum.T_C0003093.getCode(), e.getMessage(), e);
        }
        validJsonSchema(valNode,schemaNode,jsonStatus);
    }

    /**
     * 通过Jsonschema校验json体是否合法
     * @param params     json体
     * @param schemaJson json校验体
     */
    public static void validJsonSchema(Map<String, Object> params, String schemaJson, String jsonStatus) {
        ObjectMapper mapper = JsonUtil.getMapperInstant();
        JsonNode valNode = mapper.valueToTree(params);
        JsonNode schemaNode;
        try {
            schemaNode = mapper.readTree(schemaJson);
        } catch (JsonProcessingException e) {
            throw new FlowException(ErrorCodeEnum.T_C0003093.getCode(), e.getMessage(), e);
        }
        validJsonSchema(valNode,schemaNode,jsonStatus);
    }

    /**
     * 通过Jsonschema校验json体是否合法
     * @param valNode     json体
     * @param schemaNode json校验体
     */
    public static void validJsonSchema(JsonNode valNode, JsonNode schemaNode, String jsonStatus) {
        ProcessingReport report = null;
        try {
            report = FACTORY.getValidator()
                    .validate(schemaNode, valNode);
        } catch (Exception e) {
            if (Objects.equals(JSON_SCHEMA_BLOCK_ON,jsonStatus)) {
                throw new FlowException(ErrorCodeEnum.T_C0003093.getCode(), e.getMessage(), e);
            }
            LOGGER.error("JsonSchema validate exception:[{}]", e.getMessage(), e);
        }
        if (report != null && !report.isSuccess()) {
            StringBuilder errInfo = new StringBuilder();
            for (ProcessingMessage errMsg : report) {
                JsonNode node = errMsg.asJson();
                errInfo.append("[")
                        .append(node.at("/instance/pointer").asText())
                        .append("] ")
                        .append(node.get("message").asText().replaceAll("\"", ""))
                        .append(";");
            }
            LOGGER.error("JsonSchema Check Failed:[{}]", errInfo);
            // 开关打开，阻断流程
            if (Objects.equals(JSON_SCHEMA_BLOCK_ON,jsonStatus)) {
                throw new FlowException(ErrorCodeEnum.T_C0003093.getCode(), errInfo.toString());
            }
        }
    }
}
