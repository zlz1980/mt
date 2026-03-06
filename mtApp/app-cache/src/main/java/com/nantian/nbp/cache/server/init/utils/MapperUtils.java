/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.cache.server.init.utils;

import com.nantian.nbp.cache.server.init.mapper.InitAllTranCodeMapper;
import com.nantian.nbp.cache.server.init.mapper.InitApiMapper;
import com.nantian.nbp.cache.server.init.mapper.InitDecisionStepMapper;
import com.nantian.nbp.cache.server.init.mapper.InitFlowMapper;
import com.nantian.nbp.cache.server.init.mapper.InitFlowParaMapper;
import com.nantian.nbp.cache.server.init.mapper.InitHttpConfMapper;
import com.nantian.nbp.cache.server.init.mapper.InitSqlTemplateMapper;
import com.nantian.nbp.cache.server.init.mapper.InitStaticTablesMapper;
import com.nantian.nbp.cache.server.init.mapper.InitSysCfgMapper;
import com.nantian.nbp.cache.server.init.mapper.InitTranCodeConvMapper;
import com.nantian.nbp.cache.server.init.mapper.InitUtilsBeanMapper;
import com.nantian.nbp.cache.server.init.mapper.saga.InitSagaMapper;
import org.apache.ibatis.session.Configuration;

public class MapperUtils {
    public static void addMappers(Configuration configuration){
        addMapper(configuration,InitFlowMapper.class);
        addMapper(configuration,InitFlowParaMapper.class);
        addMapper(configuration,InitStaticTablesMapper.class);
        addMapper(configuration,InitAllTranCodeMapper.class);
        addMapper(configuration,InitSqlTemplateMapper.class);
        addMapper(configuration,InitTranCodeConvMapper.class);
        addMapper(configuration,InitHttpConfMapper.class);
        addMapper(configuration,InitSysCfgMapper.class);
        addMapper(configuration,InitSagaMapper.class);
        addMapper(configuration, InitUtilsBeanMapper.class);
        addMapper(configuration, InitApiMapper.class);
        addMapper(configuration, InitDecisionStepMapper.class);
    }

    public static void addMapper(Configuration configuration, Class<?> type){
        if(!configuration.hasMapper(type)){
            configuration.addMapper(type);
        }
    }
}
