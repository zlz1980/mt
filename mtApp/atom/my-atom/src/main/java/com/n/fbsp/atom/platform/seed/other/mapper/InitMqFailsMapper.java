package com.n.fbsp.atom.platform.seed.other.mapper;



import com.n.fbsp.atom.platform.seed.other.po.MqFailMessage;
import org.apache.ibatis.annotations.Insert;

/**
 * @Author : 
 * @create 2024/2/28 14:18
 */
public interface InitMqFailsMapper {

    @Insert("INSERT INTO T_PB_MQBATCH_FAILS (mqId,topic,mqMessage,status,nodeId,execTimes,chnlNo,fTranCode,regYear) VALUES (#{mqId}," +
            "#{topic},#{mqMessage},#{status},#{nodeId},#{execTimes},#{chnlNo},#{fTranCode},YEAR(curdate()))")
    int insertMqFailsMessage(MqFailMessage mqFailMessage);

}
