package org.jts.cache.resource.impl;

import org.jts.cache.resource.PbResourceCache;
import org.jts.cache.resource.ResourceCacheLoader;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class NewCodeCacheLoader extends ResourceCacheLoader<List<String>> {

    public static final String RESOURCE_NAME = "NewCode";

    public static final String KEY = "list";

    private final SqlSessionTemplate sqlSessionTemplate;

    public NewCodeCacheLoader(SqlSessionTemplate sqlSessionTemplate) {
        super(RESOURCE_NAME);
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public PbResourceCache<List<String>> initCache() {
        List<String> list = sqlSessionTemplate.selectList("mtgw.sql.cache.newcode.findAllNewCode");
        if(!CollectionUtils.isEmpty(list)){
            PbResourceCache<List<String>> resourceCache = new PbResourceCache<>(getResourceName(),list.size());
            resourceCache.put(KEY,list);
            return resourceCache;
        }
        return null;
    }
}
