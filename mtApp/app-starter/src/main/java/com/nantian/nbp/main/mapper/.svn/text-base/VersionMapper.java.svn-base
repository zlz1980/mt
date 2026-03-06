package com.nantian.nbp.main.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VersionMapper {
    @Select("select defvalue from t_pb_sys_cfg where defname = 'cache-version'")
    String findVersion();
}
