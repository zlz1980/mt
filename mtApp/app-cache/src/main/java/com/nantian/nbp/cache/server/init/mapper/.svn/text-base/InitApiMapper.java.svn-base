package com.nantian.nbp.cache.server.init.mapper;

import com.nantian.nbp.base.model.Api;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface InitApiMapper {
    @Select("select busitype,apicode, jstype,jsinfo from t_pb_ext_api")
    List<Api> findAllApiInfo();

}
