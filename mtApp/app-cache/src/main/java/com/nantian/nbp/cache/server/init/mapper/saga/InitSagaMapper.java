package com.nantian.nbp.cache.server.init.mapper.saga;

import com.nantian.nbp.saga.SagaRollbackFlow;
import com.nantian.nbp.saga.SagaRollbackStep;
import com.nantian.nbp.saga.SagaStepKey;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;

import java.util.List;

/**
 * @author Administrator
 */
public interface InitSagaMapper {

    @Insert("INSERT INTO T_PB_EXT_SAGA_ROLLBACK_FLOW (bizId,chnlNo,fTranCode,bizType,tranCode,status) " +
            "VALUES (#{bizId},#{chnlNo},#{fTranCode},#{bizType},#{tranCode},#{status})")
    int insertSagaRollbackFlow(SagaRollbackFlow sagaRollbackFlow);

    @Insert("INSERT INTO T_PB_EXT_SAGA_ROLLBACK_STEP (bizId,stepNo,inParamJson,rbfTranCode,status) VALUES " +
            "(#{bizId},#{stepNo},#{inParamJson},#{rbfTranCode},#{status})")
    int insertSagaRollbackStep(SagaRollbackStep sagaRollbackStep);

    @Select("SELECT defValue FROM T_PB_SYS_CFG WHERE defName='SAGAQUE_THREADPOOL_SIZE'")
    String selectSagaMgrQueThreadPoolSize();
}
