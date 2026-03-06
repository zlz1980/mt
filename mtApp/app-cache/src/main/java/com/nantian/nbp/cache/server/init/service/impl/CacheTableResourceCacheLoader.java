/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.cache.server.init.service.impl;

import com.nantian.nbp.base.model.StaticTable;
import com.nantian.nbp.cache.server.ResourceCacheLoader;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.mapper.InitStaticTablesMapper;
import com.nantian.nbp.utils.StrUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static com.nantian.nbp.cache.server.api.Constants.APP_CACHE_ERR_KEY;
import static com.nantian.nbp.utils.Constants.ATOM_RESULT_ATTR_SIZE;
import static com.nantian.nbp.utils.Constants.PARAM_SPILT_FLAG;
import static com.nantian.nbp.utils.Constants.STATIC_DATA_KEY_DELIMITER;

public class CacheTableResourceCacheLoader extends ResourceCacheLoader<Map<String, Object>> {
    private final static Logger LOGGER = LoggerFactory.getLogger(CacheTableResourceCacheLoader.class);

    private final SqlSessionTemplate sqlSessionTemplate;

    public CacheTableResourceCacheLoader(String resourceName, SqlSessionTemplate sqlSessionTemplate) {
        super(resourceName);
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public PbResourceCache<Map<String, Object>> initCache() {
        InitStaticTablesMapper initStaticTablesMapper = sqlSessionTemplate.getMapper(InitStaticTablesMapper.class);
        List<StaticTable> staticTableList = initStaticTablesMapper.findAllStaticTable();
        PbResourceCache<Map<String, Object>> resourceCache = new PbResourceCache<>(getResourceName(), staticTableList.size());
        if (!ObjectUtils.isEmpty(staticTableList)) {
            for (StaticTable staticTable : staticTableList) {
                String cacheId = staticTable.getCacheId();
                String keyName = staticTable.getKeyName();
                String sql = staticTable.getExecSql();
                if (sql.contains("*")) {
                    throw new RuntimeException(String.format(APP_CACHE_ERR_KEY + "CacheTableResourceCacheLoader: SQL语句包含星号，定义不合规 [%s]", sql));
                }
                Map<String, Object> currSqlCacheData = new HashMap<>();
                resourceCache.put(cacheId, currSqlCacheData);
                //处理keyName参数
                String[] keyNames = StringUtils.delimitedListToStringArray(keyName, PARAM_SPILT_FLAG);
                List<Map<String, Object>> list = initStaticTablesMapper.findStaticTableBySql(sql);
                if (!ObjectUtils.isEmpty(list)) {
                    for (Map<String, Object> value : list) {
                        StringJoiner cacheKey = new StringJoiner(STATIC_DATA_KEY_DELIMITER);
                        for (String s : keyNames) {
                            s = s.toUpperCase();
                            cacheKey.add(StrUtils.toStrDefBlank(value.get(s)));
                        }
                        String sCacheKey = cacheKey.toString();
                        if (currSqlCacheData.containsKey(sCacheKey)) {
                            throw new RuntimeException(APP_CACHE_ERR_KEY + String.format("CacheTableResourceCacheLoader: tableKey [%s] 重复", sCacheKey));
                        }
                        if (value.containsKey(ATOM_RESULT_ATTR_SIZE)) {
                            throw new RuntimeException(APP_CACHE_ERR_KEY + String.format("CacheTableResourceCacheLoader: [%s] sql结果列名不允许使用size，与相关原子交易保留属性名冲突", cacheId));
                        }
                        currSqlCacheData.put(sCacheKey, value);
                        LOGGER.info("staticTableKey [{}] staticTableValue [{}]", sCacheKey, value);
                    }
                }
            }
        }
        return resourceCache;
    }
}
