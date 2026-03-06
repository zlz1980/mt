/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.rule.cache.server.init.util;

import com.nantian.nbp.rule.cache.server.init.mapper.rule.InitRuleBizMapper;
import org.apache.ibatis.session.Configuration;

public class RuleMapperUtils {
    public static void addMappers(Configuration configuration){
        addMapper(configuration, InitRuleBizMapper.class);
    }

    public static void addMapper(Configuration configuration, Class<?> type){
        if(!configuration.hasMapper(type)){
            configuration.addMapper(type);
        }
    }
}
