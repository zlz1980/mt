package com.nantian.nbp.flow.engine.service.api.saga;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.base.model.SagaTransitionManager;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.SagaAtomResult;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 为定义某个原子交易在发生异常时，需要进行统一的事务处理登记，需要继承此事务型原子交易抽象进行实现。
 *
 * @author Administrator
 */
public abstract class AbstractSagaAtomService implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSagaAtomService.class);

    /**
     * 重写doService方法以提供Saga原子操作的结果
     *
     * @param tranContext
     * @param scopeValUnit 作用域值单元，表示在当前操作范围内的值
     * @param flowUnit     流单元，代表流程中的一个单元操作
     * @return 返回一个新的SagaAtomResult对象，表示Saga原子操作的结果
     */
    @Override
    public AtomResult doService(TranContext tranContext, final ScopeValUnit scopeValUnit, final FlowUnit flowUnit) {
        return new SagaAtomResult();
    }

    /**
     * 注册到当前流程的分布式事务流程中
     *
     * @param tranContext  事物管理器
     * @param scopeValUnit 当前步骤作用域
     * @param flowUnit     步骤单元信息
     */
    protected abstract void registerSagaStep(final TranContext tranContext, final ScopeValUnit scopeValUnit,
                                             final FlowUnit flowUnit);

    /**
     * 处理异常的抽象方法
     * <p>
     * 此方法旨在为子类提供一个模板，用于处理可能在执行流程中遇到的各种异常情况
     * 它结合了当前的作用域值单元、流程单元以及捕获的异常，以便进行特定的异常处理逻辑
     *
     * @param e            捕获到的异常，提供了异常的详细信息
     * @param tranContext
     * @param scopeValUnit 当前的作用域值单元，包含了与异常发生时相关的作用域信息
     * @param flowUnit     当前的流程单元，代表了异常发生时的执行流程上下文
     */
    protected abstract void exceptionProcess(Exception e, TranContext tranContext, final ScopeValUnit scopeValUnit, final FlowUnit flowUnit);

    /**
     * 处理未知异常的方法
     * <p>
     * 当遇到无法预期的异常时，此方法被调用以执行特定的异常处理逻辑它主要负责在特定的业务范围内处理异常，
     * 并根据情况更新业务流程的状态
     *
     * @param e            异常对象，代表了发生的异常
     * @param tranContext  交易上下文访问接口
     * @param scopeValUnit 业务范围单元，提供了异常处理的业务上下文
     * @param flowUnit     流程单元，代表了当前的业务流程，用于访问和修改流程相关的信息
     */
    protected void processUnKnowException(Exception e, final TranContext tranContext, ScopeValUnit scopeValUnit, final FlowUnit flowUnit) {
        // 获取业务流程参数，用于判断是否需要进行异常处理
        if (isSagaParamAndManagerExist(tranContext, flowUnit)) {
            // 获取Saga流转管理器，用于管理业务流程的流转状态
            SagaTransitionManager sagaTransitionManager = tranContext.getSagaTransitionManager();
            // 当sagaParam有文本时，执行异常处理逻辑，并设置触发标志
            exceptionProcess(e, tranContext, scopeValUnit, flowUnit);
            /* 若exceptionProcess中触发异常，则由底层主控服务无条件执行整个事务反向操作，不依赖下面的triggerFlag设置操作 */
            sagaTransitionManager.setTriggerFlag(true);
        }
    }

    /**
     * 设置 Saga 控制标志位，根据给定的处理结果和业务返回类型。
     *
     * @param tranContext 作用域值单元，用于存储和传递 Saga 流程中的变量
     * @param flowUnit    流程单元，包含当前流程的详细信息和参数
     */
    protected void regSagaTransitionManager(final TranContext tranContext, final ScopeValUnit scopeValUnit, final FlowUnit flowUnit) {
        if (isSagaParamAndManagerExist(tranContext, flowUnit)) {
            SagaTransitionManager sagaTransitionManager = tranContext.getSagaTransitionManager();
            if (sagaTransitionManager.isTriggerFlag()) {
                LOGGER.error("当前事务管理器已触发回滚，不应再登记新增分支!");
                throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(),
                        T_X0005091.getCodeMsg("当前事务管理器已触发回滚，不应再登记新增分支!"), null);
            }
            // 注册 Saga 步骤，将当前流程单元的信息注册到 Saga 转移管理器中
            registerSagaStep(tranContext, scopeValUnit, flowUnit);
        }
    }

    /**
     * 设置Saga管理器触发标志
     * 该方法用于在特定条件下设置SagaTransitionManager的触发标志为true
     * 条件是FlowUnit中的sagaParam不为空且SagaTransitionManager对象不为null
     *
     * @param tranContext 包含SagaTransitionManager的ScopeValUnit对象
     * @param flowUnit    包含sagaParam的FlowUnit对象
     */
    protected void setSagaManagerTriggerFlag(final TranContext tranContext, final FlowUnit flowUnit) {
        if (isSagaParamAndManagerExist(tranContext, flowUnit)) {
            // 获取ScopeValUnit中的SagaTransitionManager对象
            SagaTransitionManager sagaTransitionManager = tranContext.getSagaTransitionManager();
            // 设置SagaTransitionManager的触发标志为true
            sagaTransitionManager.setTriggerFlag(true);
        }
    }

    /**
     * 检查给定的ScopeValUnit中是否存在有效的SagaTransitionManager以及FlowUnit中是否存在sagaParam参数
     * 此方法用于确保在流程中使用Saga模式时，所需的参数和管理器都已正确配置和初始化
     *
     * @param tranContext 包含SagaTransitionManager的ScopeValUnit对象，用于管理Saga流程的状态转换
     * @param flowUnit    包含sagaParam参数的FlowUnit对象，代表流程中的一个步骤
     * @return 如果sagaParam参数存在且SagaTransitionManager不为null，则返回true；否则返回false
     * <p>
     * 该方法首先从ScopeValUnit中获取SagaTransitionManager对象，然后从FlowUnit中获取sagaParam参数
     * 接着，它检查sagaParam参数是否不为空，如果为空，则直接返回false，表示Saga参数不存在
     * 如果sagaParam参数不为空，它进一步检查SagaTransitionManager对象是否不为null，如果为null，则抛出FlowException异常，
     * 表示虽然设置了Saga参数，但未初始化Saga管理器，这可能导致Saga流程无法正确执行
     */
    private boolean isSagaParamAndManagerExist(final TranContext tranContext, final FlowUnit flowUnit) {
        // 获取ScopeValUnit中的SagaTransitionManager对象
        SagaTransitionManager sagaTransitionManager = tranContext.getSagaTransitionManager();
        // 获取FlowUnit中的sagaParam参数
        String sagaParam = flowUnit.getSagaParam();
        // 检查sagaParam是否不为空
        if (StringUtils.hasText(sagaParam)) {
            // 检查SagaTransitionManager对象是否不为null
            if (Objects.nonNull(sagaTransitionManager)) {
                return true;
            } else {
                // 记录错误日志，提示sagaParam已设置但SagaTransitionManager未初始化
                LOGGER.error("当前步骤设置了saga事务参数[{}],但saga事务管理器未初始化,请检查!", sagaParam);
                // 抛出FlowException异常，指示Saga管理器未初始化的问题
                throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(),
                        T_X0005091.getCodeMsg("当前步骤设置了saga事务参数[{}],但saga事务管理器未初始化,请检查!",
                                sagaParam), null);
            }
        }
        // 获取SagaTransitionManager对象
        return false;
    }
}
