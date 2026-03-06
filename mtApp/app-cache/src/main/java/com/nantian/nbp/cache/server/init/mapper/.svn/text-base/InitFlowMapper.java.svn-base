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
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.base.model.TranCode;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 初始化流程服务数据访问
 * @author Administrator
 */
public interface InitFlowMapper {

	/**
	 * 查询所有的TranCodeKey
	 * @return List<FlowKey>
	 */
	@Select("SELECT DISTINCT BUSITYPE,TRANCODE FROM T_PB_TRAN_CODE tptc ORDER BY BUSITYPE,TRANCODE asc")
    List<FlowKey> findAllTranCodeKey();


	@Select("SELECT DISTINCT BUSITYPE,TRANCODE,EXTENDCODE FROM T_PB_TRAN_CODE WHERE TRIM(EXTENDCODE) <>'' ")
    List<TranCode> findExtendTranCodeInfo();

	@Select("SELECT A.TRANCODE, A.BUSITYPE, A.ATOMTRANNO, A.ATOMTRANCODE, A.ATOMTRANPARAM AS SRCATOMTRANPARAM, A.ABNPROCTYPE,"
			+"A.NOTE, A.ERRCODE,A.ERRORINFO,A.ASCOPE,A.EXTENDPARAFLAG,A.SAGAPARAM FROM T_PB_FLOW A "
			+"ORDER BY BUSITYPE,TRANCODE,ATOMTRANNO ASC")
	List<FlowUnit> findFlowUnit();
}