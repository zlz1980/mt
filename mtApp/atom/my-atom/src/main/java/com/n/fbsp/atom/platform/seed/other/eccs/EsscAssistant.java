package com.n.fbsp.atom.platform.seed.other.eccs;

import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;


import static com.n.fbsp.atom.platform.seed.other.utils.Constants.EC_CONF_CONF_VALUE_KEY;
import static com.n.fbsp.atom.platform.seed.other.utils.Constants.EC_CONF_STATBL_KEY;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * @创建人 fbsp
 * @创建时间 2025/6/12 18:01
 * @描述 加密平台工具类
 */
@Component
public final class EsscAssistant {
    private static final Logger logger = LoggerFactory.getLogger(EsscAssistant.class);

    @Autowired
    private CacheClientApi cacheClientApi;

    /**
     * 加密平台SDK的对象esscApi实例
     */
    /*private EsscAPI esscApiInstance;*/

    /**
     * 获取加密平台SDK的对象esscApi实例的入口
     */
    /*public EsscAPI getEsscAPI() throws Exception{
        if (this.esscApiInstance == null) {
            synchronized (EsscAssistant.class) {
                if (this.esscApiInstance == null) {
                    this.esscApiInstance = new EsscAPI(cacheClientApi.getSysCfg(Constants.EC_CFG_PATH));
                    this.esscApiInstance.Init();
                    logger.info("加密平台EsscAPI init success,EsscVersion is ",EsscVersion.getVersion());
                }
            }
        }
        return esscApiInstance;
    }*/

    /**
     * 根据提供的键名获取对应的秘钥
     * <p>
     * 此方法首先从缓存中获取与给定键名相关的配置信息如果找到了对应的配置信息，
     * 则提取并返回秘钥值如果未找到有效的秘钥值，则记录错误日志并抛出异常
     *
     * @param keyName 要获取秘钥的键名
     * @return 与键名关联的秘钥值
     *
     * @throws FlowException 如果未能获取到秘钥值，则抛出此异常
     */
    public String getEsscKeyName(String keyName) {
        // 获取pin或者Mac使用的秘钥名称
        String esscKey = "";
        Map<String, Object> esscKeyResult = cacheClientApi.getStaticTable(EC_CONF_STATBL_KEY, keyName);
        if (!ObjectUtils.isEmpty(esscKeyResult)) {
            esscKey = esscKeyResult.get(EC_CONF_CONF_VALUE_KEY).toString();
        }
        if (!StringUtils.hasText(esscKey)) {
            throw new FlowException(T_X0005091.getCode(), T_X0005091.getCodeMsg("获取[{}]秘钥名称失败,请检查加密平台配置信息表T_PB_ESSC_CONF信息!", keyName));
        }
        return esscKey;
    }
}
