package com.nantian.nbp.flow.extend.ae;

import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.FlowPara;
import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.base.model.bo.FlowUnitBo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
public interface FlowExtendMapper {
    @Select("select m.AREANO, m.BUSITYPE, m.TRANCODE, m.TRANNAME, m.TRANTYPE, m.TRANCHAR, m.JOURFLAG, m.TRANLEVEL, " +
        "m.MODITIMES, m.MODILEVEL, m.TRANSTATUS, m.TRANEXPLAIN, m.CREATETLR, m.CREATEDATE, m.UPTLRNO, " +
        "m.UPDATEDATE, m.NOTE, m.OWNER, m.OWNLEVEL, m.OWNFLAG, m.LEDITOR, m.LETIME, m.CRTIME, m.EXTENDCODE, " +
        "m.APICODE, m.ISBLOCKED from T_PB_TRAN_CODE m where busitype = #{busiType} and tranCode = #{tranCode}")
    TranCode findTranInfo(FlowKey flowkey);

    @Select("SELECT AREANO,BUSITYPE,TRANCODE,ASCOPE,ATOMTRANNO,ATOMPARA,ATOMTRANCODE,NOTE,RETVALUE FROM T_PB_FLOW_PARA where busitype = #{busiType}" +
        " and tranCode = #{tranCode} ORDER BY ATOMTRANNO,RETVALUE ASC")
    List<FlowPara> findFlowParaList(FlowKey flowkey);

    @Select("SELECT A.AREANO,A.TRANCODE, A.BUSITYPE, A.ATOMTRANNO, A.ATOMTRANCODE, A.ATOMTRANPARAM, A.ABNPROCTYPE," +
        "A.NOTE, A.ERRCODE,A.ASCOPE,A.EXTENDPARAFLAG,A.SAGAPARAM," +
        "case " +
        " when a.atomtrancode = 'MODULE' and length(A.ATOMTRANPARAM) = 9 " +
        " then (select TRANNAME from T_PB_TRAN_CODE where CONCAT(BUSITYPE,TRANCODE) = A.ATOMTRANPARAM)" +
        " else b.ATOMTRANNAME END " +
        " as ATOMTRANNAME," +
        "case when (A.ERRORINFO = '' or A.ERRORINFO is null) and A.ERRCODE != '0'" +
        " then C.ERRORINFO" +
        " else A.ERRORINFO end" +
        " as ERRORINFO" +
        " FROM T_PB_FLOW A " +
        " left join t_pb_atom_tran B on A.atomtrancode = b.ATOMTRANCODE " +
        " left join T_PB_ext_ERROR_CODE C on A.ERRCODE = C.ERRORCODE" +
        " where" +
        " busitype = #{busiType} and tranCode = #{tranCode}" +
        "ORDER BY ATOMTRANNO ASC")
    List<FlowUnitBo> findFlowUnitList(FlowKey flowkey);

}
