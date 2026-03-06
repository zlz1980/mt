package com.n.fbsp.atom.platform.seed.other.mapper;

import com.nantian.nbp.base.model.PbLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Administrator
 */
@Mapper
public interface InitPbLogMapper {

    @Insert("INSERT INTO t_pb_log (tranDate,tranTime,bizId,gTrace,reqChnl,fTranCode,busiType,tranCode,pbTime," +
            "serviceStatus,tranStatus,logPath,respCode,respMsg) VALUES (#{tranDate},#{tranTime},#{bizId},#{gTrace}," +
            "#{reqChnl},#{fTranCode},#{busiType},#{tranCode},#{pbTime},#{serviceStatus},#{tranStatus},#{logPath}," +
            "#{respCode},#{respMsg})")
    int insertPbLog(PbLog pbLog);
}
