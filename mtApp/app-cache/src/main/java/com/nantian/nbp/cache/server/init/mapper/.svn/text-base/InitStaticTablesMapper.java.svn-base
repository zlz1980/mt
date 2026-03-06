package com.nantian.nbp.cache.server.init.mapper;

import com.nantian.nbp.base.model.StaticTable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface InitStaticTablesMapper {


	/**
	 * 查询所有静态表信息
	 *
	 * 该方法通过MyBatis的注解方式，执行SQL查询，返回所有静态表的列表
	 * 主要用于获取数据库中T_PB_STATICTBL_APP表的所有记录，这些记录包含了静态表的缓存ID、键名和执行SQL
	 *
	 * @return 返回一个包含所有静态表信息的列表，列表中的每个元素都是一个StaticTable对象
	 */
	@Select("SELECT cacheid,keyname,execsql FROM T_PB_STATICTBL_APP")
	List<StaticTable> findAllStaticTable();

    /**
     * 查询所有静态表信息
     *
     * 该方法通过MyBatis的注解方式，执行SQL查询，返回所有静态表的列表
     * 主要用于获取数据库中T_PB_STATICTBL_APP表的所有记录，这些记录包含了静态表的缓存ID、键名和执行SQL
     *
     * @return 返回一个包含所有静态表信息的列表，列表中的每个元素都是一个StaticTable对象
     */
    @Select("SELECT cacheid,keyname,execsql FROM T_PB_STATICTBL_APP where cacheid = 't_eb_cardinf'")
    StaticTable findCardBinStaticTable();

	@Select("${sql}")
    List<Map<String,Object>> findStaticTableBySql(@Param("sql") String sql);

}
