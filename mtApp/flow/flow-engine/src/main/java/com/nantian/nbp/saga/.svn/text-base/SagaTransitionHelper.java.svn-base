package com.nantian.nbp.saga;

import com.nantian.nbp.base.model.SagaTransitionManager;
import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.Optional;

public class SagaTransitionHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SagaTransitionHelper.class);

    /**
     * 触发事务回滚，此时停止记录回滚步骤
     * @param feContext 流程上下文
     */
    public static void trigger(FeContext feContext,boolean triggerFlag){
        Optional.ofNullable(feContext)
                .map(FeContext::getSagaTransitionManager)
                .ifPresent(manager -> manager.setTriggerFlag(triggerFlag));
    }

    /**
     * 触发事务回滚，此时停止记录回滚步骤
     * @param manager 事务管理器
     */
    public static void trigger(SagaTransitionManager manager){
        if(Objects.nonNull(manager)){
            manager.setTriggerFlag(Boolean.TRUE);
        }
    }

    /**
     * 判断流程是否开启事务，异步提交事务信息
     * @param feContext 流程上下文
     */
    public static void recode(FeContext feContext){
        // 检查feContext是否为null
        if (!ObjectUtils.isEmpty(feContext)) {
            SagaTransitionManager transitionManager = feContext.getSagaTransitionManager();
            if(!ObjectUtils.isEmpty(transitionManager)){
                // 检查触发标志是否为true且事务非空
                if(transitionManager.isNotEmpty()) {
                    if(transitionManager.isTriggerFlag()){
                        LOGGER.info("异步登记冲正流程信息:[{}]", transitionManager.getSagaRollbackFlow().toString());
                        LOGGER.info("异步登记冲正步骤信息:[{}]", transitionManager.getStepList().toString());
                        // 异步提交事务信息
                        SagaTransitionHelper.commit(transitionManager);
                    }else{
                        LOGGER.info("事务回滚标志[{}],跳过事务异步登记", false);
                    }
                }
            }
        }
    }

    public static void commit(SagaTransitionManager sagaTransitionManager) {
        SagaManageQueue.getInstance().add(sagaTransitionManager);
    }

}
