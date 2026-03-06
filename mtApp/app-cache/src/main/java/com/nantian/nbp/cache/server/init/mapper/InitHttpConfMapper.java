package com.nantian.nbp.cache.server.init.mapper;

import com.nantian.nbp.base.model.HttpConf;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Administrator
 */
public interface InitHttpConfMapper {

    @Select("SELECT m.serName,m.beanName,m.gatewayurl,m.retcodefieldname,m.unknownretcodeset,m.successretcodeset,m.reterrmsgfieldname,m.reterrcodefieldname,m.sysabbr,m.reqdatefieldname,m.innerbusikeyfieldname FROM T_PB_EXT_HTTP_CONF m ")
    List<HttpConf> findAllHttpConfList();
}
