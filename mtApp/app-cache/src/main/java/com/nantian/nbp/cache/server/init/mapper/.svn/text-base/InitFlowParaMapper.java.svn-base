/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.cache.server.init.mapper;

import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.FlowPara;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* 流程增强处理数据库访问
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public interface InitFlowParaMapper {

    @Select("SELECT BUSITYPE,TRANCODE,ASCOPE,ATOMTRANNO,ATOMPARA FROM T_PB_FLOW_PARA  ORDER BY BUSITYPE,TRANCODE,ASCOPE ASC")
    List<FlowPara> findFlowPara();

    @Select("SELECT DISTINCT BUSITYPE,TRANCODE FROM T_PB_FLOW_PARA ORDER BY BUSITYPE,TRANCODE ASC")
    List<FlowKey> findAllFlowKey();

}
