package com.n.fbsp.atom.platform.seed.util.bean;

import com.nantian.nbp.flow.engine.service.api.Util;
import com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @创建人 周围
 * @创建时间 2025/6/25 18:23
 * @描述
 */
@Util("JsonUtils")
public class JsonUtilsBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtilsBean.class);


    public static Map<String, Object> jsonToMap(String value) {
        // 首先，检查原字符串是否为空
        if (!StringUtils.hasText(value)) {
            LOGGER.error("JsonUtilsBean工具类JSON转MAP异常，入参为空");
            throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("JsonUtilsBean工具类JSON转MAP异常，入参为空"));
        }
        try {
            return JsonUtil.strToMap(value);
        }catch (Exception e){
            LOGGER.error("JsonUtilsBean工具类JSON转MAP异常，请检查入参[{}]", value);
            throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("JsonUtilsBean工具类JSON转MAP异常，请检查入参[{}]", value));
        }
    }

    public static String mapToJson(Map map) {
        try {
            // 首先，检查原字符串是否为空
            if (CollectionUtils.isEmpty(map)) {
                LOGGER.error("JsonUtilsBean工具类MAP转JSON异常，入参为空");
                throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("JsonUtilsBean工具类MAP转JSON异常，入参为空"));
            }
            return JsonUtil.objToString(map);
        }catch (Exception e){
            LOGGER.error("JsonUtilsBean工具类MAP转JSON异常，请检查入参[{}]", e.getMessage());
            throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), ErrorCodeEnum.T_C0003092.getCodeMsg("JsonUtilsBean工具类MAP转JSON异常，请检查入参[{}]", e.getMessage()));
        }
    }

}
