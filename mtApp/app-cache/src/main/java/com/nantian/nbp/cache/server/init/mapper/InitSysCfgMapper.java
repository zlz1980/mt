package com.nantian.nbp.cache.server.init.mapper;

import com.nantian.nbp.base.model.SysCfg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface InitSysCfgMapper {

    @Select("SELECT defName,defValue FROM T_PB_SYS_CFG")
    List<SysCfg> findAllSysCfgInfo();

    @Update("update T_PB_SYS_CFG set defValue =#{defValue} where defName=#{defName}")
    int updateSysCfg(@Param("defName") String defName, @Param("defValue") String defValue);

}
