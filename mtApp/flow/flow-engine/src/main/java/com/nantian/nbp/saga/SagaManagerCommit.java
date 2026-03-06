package com.nantian.nbp.saga;

import com.nantian.nbp.base.model.SagaTransitionManager;
import com.nantian.nbp.cache.server.init.mapper.saga.InitSagaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SagaManagerCommit implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SagaManagerCommit.class);
    private final InitSagaMapper mapper;

    private final Boolean loop;

    public SagaManagerCommit(InitSagaMapper mapper, Boolean loop) {
        this.mapper = mapper;
        this.loop = loop;
    }

    @Override
    public void run() {
        SagaManageQueue queue = SagaManageQueue.getInstance();
        while (loop) {
            SagaTransitionManager manager;
            try {
                manager = queue.take();
            } catch (InterruptedException e) {
                LOGGER.error("APPSAGAMGRQUEERROR: 获取事务管理器队列数据失败,异常信息:[{}]", e.getMessage());
                throw new RuntimeException(e);
            }
            SagaRollbackFlow flow = manager.getSagaRollbackFlow();
            LOGGER.info("登记冲正流程,rollbackFlow:[{}]", flow.toString());
            if(!flow.isCommit()) {
                mapper.insertSagaRollbackFlow(flow);
                flow.setCommit(true);
            }
            List<SagaRollbackStep> stepList = manager.getStepList();
            for (SagaRollbackStep step : stepList) {
                LOGGER.info("登记冲正步骤,rollbackStep:[{}]", step.toString());
                if(!step.isCommit()) {
                    mapper.insertSagaRollbackStep(step);
                    step.setCommit(true);
                }
            }
        }
    }
}
