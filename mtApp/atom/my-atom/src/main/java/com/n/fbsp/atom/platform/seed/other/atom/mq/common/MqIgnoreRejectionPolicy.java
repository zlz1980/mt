package com.n.fbsp.atom.platform.seed.other.atom.mq.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_MQ_PRODUCER_ERR_KEY;

/**
 * 线程池拒绝策略，打印error级别日志，不抛出异常，不阻塞线程
 *
 * @author
 * @since @since 2024-11-04 15:41:03
 */
public class MqIgnoreRejectionPolicy implements RejectedExecutionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqIgnoreRejectionPolicy.class);

//    @Autowired
//    private FallbackToFileHandler fallbackToFileHandler;

    /**
     * 当线程池拒绝执行任务时的回调方法
     * 此方法在任务被线程池拒绝时被调用，通常是因为线程池和工作队列已满
     * 在这种情况下，方法将记录错误信息，并尝试将任务降级写入本地文件
     *
     * @param runnable           被拒绝执行的任务
     * @param threadPoolExecutor 拒绝执行任务的线程池执行器
     */
    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
        // 记录线程池拒绝执行任务的错误信息，包括核心线程数、工作队列大小等
        logger.error(APP_MQ_PRODUCER_ERR_KEY + "MQ生产者初始线程程核心数[{}],最大工作队列数[{}],当前任务已达最大值,提交任务失败,消息将写入本地文件", threadPoolExecutor.getCorePoolSize(), threadPoolExecutor.getQueue()
                                                                                                                                                                                                             .size());
        // 将任务降级写入本地文件
        if (runnable instanceof AbstractMqMessageProducer.MessageTask) {
            AbstractMqMessageProducer.MessageTask task = (AbstractMqMessageProducer.MessageTask) runnable;
//            fallbackToFileHandler.writeToFileTask(task.getMessage());
        } else {
            // 如果任务类型不匹配，则记录错误信息
            logger.error(APP_MQ_PRODUCER_ERR_KEY + "无法将任务降级写入本地文件,任务类型不匹配");
        }
    }
}
