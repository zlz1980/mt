package com.nantian.nbp.cache.server.init.mapper;


import com.nantian.nbp.base.model.TranCode;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 流水数据库访问
 *
 */
public interface InitAllTranCodeMapper {

    @Select("select m.AREANO, m.BUSITYPE, m.TRANCODE, m.TRANNAME, m.TRANTYPE, m.TRANCHAR, m.JOURFLAG, m.TRANLEVEL, " +
            "m.MODITIMES, m.MODILEVEL, m.TRANSTATUS, m.TRANEXPLAIN, m.CREATETLR, m.CREATEDATE, m.UPTLRNO, " +
            "m.UPDATEDATE, m.NOTE, m.OWNER, m.OWNLEVEL, m.OWNFLAG, m.LEDITOR, m.LETIME, m.CRTIME, m.EXTENDCODE, " +
            "m.APICODE, m.ISBLOCKED from T_PB_TRAN_CODE m ;")
    List<TranCode> findAllTranCode();

}
