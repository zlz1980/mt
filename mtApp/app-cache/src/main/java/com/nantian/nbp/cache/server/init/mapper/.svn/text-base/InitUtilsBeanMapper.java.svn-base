package com.nantian.nbp.cache.server.init.mapper;

import com.nantian.nbp.base.model.UtilsBean;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface InitUtilsBeanMapper {
    @Select("SELECT utils.BEANNAME FROM T_PB_EXT_UTILS_BEAN utils ")
    List<UtilsBean> findAllUtilsBeanList();
}
