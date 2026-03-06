package com.n.fbsp.atom.platform.seed.db.seq.mapper;

import com.n.fbsp.atom.platform.seed.db.seq.SeqNum;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SeqNumMapper {
    String SEQ_NUM_TABLE = "tech_seq_num";

    int PUB_SEQ_TYPE = 0;

    int PER_SEQ_TYPE = 1;

    @Select("SELECT seq_id seqId,seq_group seqGroup,seq_key seqKey,seq_val seqVal,init_val initVal,max_val maxVal," +
            "seq_expire seqExpire,create_time createTime,TIMESTAMPDIFF(second,create_time,now()) remainingTime FROM " +SEQ_NUM_TABLE+
            " WHERE seq_group = #{seqGroup} and seq_key = #{seqKey} and seq_type = #{seqType} FOR UPDATE")
    SeqNum findSeqNumVal(@Param("seqType") int seqType, @Param("seqGroup") String seqGroup, @Param("seqKey") String seqKey);

    @Select("SELECT count(seq_key) FROM "+SEQ_NUM_TABLE+" WHERE seq_group = #{seqGroup} and seq_key = #{seqKey} and seq_type = #{seqType}")
    int countSeqNumVal(@Param("seqType") int seqType, @Param("seqGroup") String seqGroup,@Param("seqKey") String seqKey);

    @Update("UPDATE "+SEQ_NUM_TABLE+" SET seq_val = #{nxtVal} WHERE seq_group = #{seqGroup} and seq_key = #{seqKey} and seq_type = #{seqType}")
    int updateSeqNumVal(@Param("seqType") int seqType,@Param("seqGroup") String seqGroup, @Param("seqKey") String seqKey,@Param("nxtVal") long nxtVal);

    @Update("UPDATE "+SEQ_NUM_TABLE+" SET seq_val = #{nxtVal},create_time = now() WHERE seq_group = #{seqGroup} and seq_key = #{seqKey} and seq_type = #{seqType}")
    int updateRestSeqNumVal(@Param("seqType") int seqType, @Param("seqGroup") String seqGroup,@Param("seqKey") String seqKey,@Param("nxtVal") long nxtVal);

    @Insert("INSERT INTO "+SEQ_NUM_TABLE+"(seq_type,seq_group,seq_key,seq_val,init_val,max_val,seq_expire) VALUES (#{seqType},#{seqGroup},#{seqKey}," +
            "#{seqVal},#{initVal},#{maxVal},#{seqExpire})")
    int insertSeqNum(@Param("seqType") int seqType,@Param("seqGroup") String seqGroup,@Param("seqKey") String seqKey,
                     @Param("seqVal") long seqVal,@Param("initVal") long initVal,
                     @Param("maxVal") long maxVal,@Param("seqExpire") int seqExpire);

    @Delete("DELETE FROM "+SEQ_NUM_TABLE+" WHERE seq_group = #{seqGroup} and seq_key = #{seqKey} and seq_type = #{seqType}")
    int delSeqNumVal(@Param("seqType") int seqType,@Param("seqGroup") String seqGroup,@Param("seqKey") String seqKey);
}
