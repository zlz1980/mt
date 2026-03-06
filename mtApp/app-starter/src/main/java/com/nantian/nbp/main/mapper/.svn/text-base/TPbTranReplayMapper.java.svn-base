package com.nantian.nbp.main.mapper;


import com.nantian.nbp.main.po.TPbTranReplay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author : Liang Haizhen
 * @create 2024/10/29 14:18
 */
@Mapper
public interface TPbTranReplayMapper {

    @Update("update t_pb_tran_replay set replayResult=#{tPbTranReplay.replayResult} where id = #{tPbTranReplay.id}")
    int updateTranReplayInfo(@Param("tPbTranReplay") TPbTranReplay tPbTranReplay);

    @Select("select id,reqChnl,fTranCode, reqHeader,reqMsg, respMsg, replayResult, description from t_pb_tran_replay where id = #{id}")
    TPbTranReplay selectTranReplayInfo(@Param("id") int id);

}
