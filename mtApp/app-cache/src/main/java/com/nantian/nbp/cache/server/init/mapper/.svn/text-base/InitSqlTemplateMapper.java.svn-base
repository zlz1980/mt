/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.cache.server.init.mapper;

import com.nantian.nbp.base.model.SqlTemplate;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* sql模板数据库访问
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public interface InitSqlTemplateMapper {


	/**
	 * 获取全部sql模板信息
	 * @return
	 */
	@Select("SELECT sqlType,sqlName,sqlScript FROM T_PB_EXT_SQL_TEMPLATE")
    List<SqlTemplate> findAllSqlMapper();

}
