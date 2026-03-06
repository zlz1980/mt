/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.cache.server.init.mapper;

import com.nantian.nbp.base.model.TranCodeConv;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * User: jts Date: 20151030 Time: 14:32
 */

public interface InitTranCodeConvMapper {

    @Select("SELECT C.AREANO,C.CHNLNO,C.FTRANCODE,C.BUSITYPE,C.TRANCODE,P.LOGLEVEL " +
            "FROM T_PB_TRAN_CODE_CONV C LEFT JOIN T_PB_TRAN_PKG P ON C.CHNLNO = P.CHNLNO and C.FTRANCODE = P.FTRANCODE ")
    List<TranCodeConv> findTranCodeConvList();

}
