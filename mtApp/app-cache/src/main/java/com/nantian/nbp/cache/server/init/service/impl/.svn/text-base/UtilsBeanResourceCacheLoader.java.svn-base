package com.nantian.nbp.cache.server.init.service.impl;

import com.nantian.nbp.base.model.UtilsBean;
import com.nantian.nbp.cache.server.ResourceCacheLoader;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.mapper.InitUtilsBeanMapper;
import com.nantian.nbp.utils.SpringContextUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.nantian.nbp.cache.server.api.Constants.APP_CACHE_ERR_KEY;

/**
 * @author Administrator
 */
public class UtilsBeanResourceCacheLoader extends ResourceCacheLoader<UtilsBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilsBeanResourceCacheLoader.class);

    private final SqlSessionTemplate sqlSessionTemplate;


    public UtilsBeanResourceCacheLoader(String resourceName, SqlSessionTemplate sqlSessionTemplate) {
        super(resourceName);
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public PbResourceCache<UtilsBean> initCache(){
        List<UtilsBean> list = sqlSessionTemplate.getMapper(InitUtilsBeanMapper.class).findAllUtilsBeanList();
        PbResourceCache<UtilsBean> resourceCache = new PbResourceCache<>(getResourceName(),list.size());
        if(!CollectionUtils.isEmpty(list)){
            String beanName = null;
            try {
                for(UtilsBean utilsBean : list){
                    beanName = utilsBean.getBeanName();
                    utilsBean.setBean(SpringContextUtils.getBean(beanName));
                    resourceCache.put(beanName, utilsBean);
                    LOGGER.info("add bean Util[{}]",beanName);
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format(APP_CACHE_ERR_KEY + " bean Util[%s] is not support",beanName),e);
            }
        }
        return resourceCache;
    }
}
