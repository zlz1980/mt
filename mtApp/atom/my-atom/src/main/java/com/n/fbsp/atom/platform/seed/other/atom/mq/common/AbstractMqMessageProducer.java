package com.n.fbsp.atom.platform.seed.other.atom.mq.common;

import com.nantian.nbp.cache.server.api.CacheClientApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author
 * @date 2025/3/14
 */
public abstract class AbstractMqMessageProducer implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMqMessageProducer.class);
    private static boolean initialized = false;
//    protected ThreadPoolExecutorExt executor;
    @Autowired
    CacheClientApi cacheClientApi;
//    @Autowired
//    FallbackToFileHandler fallbackToFileHandler;
//    @Resource
//    private MqTemplate MqTemplate;
/*
    protected abstract boolean fallbackToLocal(MqMessage MqMessage);

    protected abstract ThreadPoolConfig getThreadPoolConfig();

    protected abstract void initThreadPool(ThreadPoolConfig threadPoolConfig);

    protected abstract String getTopic();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 初始化消息队列和线程池
        init();
    }

    public void init() {
        try {
            // 获取线程池配置，用于的初始化
            ThreadPoolConfig threadPoolConfig = getThreadPoolConfig();
            // 初始化消息队列和线程池，包括工作线程和核心参数配置等
            initThreadPool(threadPoolConfig);
            // 确保原子性地设置初始化状态为true，表示消息队列和线程池已准备好
            // 标识线程任务是否已启动
            initialized = true;
        } catch (Exception e) {
            // 日志记录初始化异常，用于诊断初始化过程中的问题
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "TOPIC[{}],MQ生产者线程池初始化异常,错误信息[{}]", getTopic(), e.getMessage(), e);
            throw new FlowException(T_X0005091.getCode(), T_X0005091.getCodeMsg("TOPIC[{}],MQ生产者线程池初始化异常,错误信息[{}]", getTopic(), e.getMessage()), e);
        }
    }

    *//**
     * 添加消息到消息队列并尝试初始化消息队列和线程池
     * 注意：该方法内部实现了线程池和消息队列的初始化逻辑，
     * 应确保线程安全和错误处理策略
     *
     * @param MqMessage 要添加的消息对象，包含需要发送的具体消息内容
     *//*
    public void addMessage(MqMessage MqMessage) {
        if (!initialized) {
            // 日志记录初始化异常，用于诊断初始化过程中的问题
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "TOPIC[{}],MQ生产者线程池未初始化,请检查!", getTopic());
            throw new FlowException(T_X0005091.getCode(), T_X0005091.getCodeMsg("TOPIC[{}],MQ生产者线程池未初始化,请检查!", getTopic()));
        }
        // 复制当前线程的MDC到子线程
        Map<String, String> parentMdc = MDC.getCopyOfContextMap();
        // 异步执行消息处理
        executor.execute(() -> {
            try {
                // 设置子线程的MDC
                MDC.setContextMap(parentMdc);
                new MessageTask(MqMessage).run();
            } finally {
                // 执行完成后清理MDC（避免资源泄漏）
                MDC.clear();
            }
        });
    }

    *//**
     * 根据指定的线程池类型获取线程池配置
     *
     * @param threadPoolMap 线程池类型，用于区分不同类型的线程池配置
     * @return 返回配置好的线程池配置对象
     *//*
    protected ThreadPoolConfig getThreadPoolConfig(HashMap<String, Object> threadPoolMap) {
        // 创建一个新的线程池配置对象
        ThreadPoolConfig config = new ThreadPoolConfig();
        // 线程池核心数
        String coreSize = (String) threadPoolMap.get(MQ_THREAD_CORE_POOL_SIZE);
        // 线程池允许的最大线程数
        String maxSize = (String) threadPoolMap.get(MQ_THREAD_MAX_POOL_SIZE);
        // 非核心线程空闲时间
        String keepSeconds = (String) threadPoolMap.get(MQ_THREAD_KEEP_ALIVE_SECONDS);
        // 线程池任务队列容量
        String queueCapacity = (String) threadPoolMap.get(MQ_THREAD_QUEUE_CAPACITY);
        // 线程池名称
        String threadPoolName = (String) threadPoolMap.get(MQ_THREAD_POOL_NAME_KEY);
        try {
            // 设置线程池配置对象的核心参数
            config.setCorePoolSize(Integer.parseInt(coreSize));
            config.setMaximumPoolSize(Integer.parseInt(maxSize));
            config.setQueueDepth(Integer.parseInt(queueCapacity));
            config.setKeepAliveTime(Integer.parseInt(keepSeconds));
            config.setPoolName(threadPoolName);
            config.setWorkQueue(new LinkedBlockingQueue<>(Integer.parseInt(queueCapacity)));
            // 采用自定义异常策略，直接抛弃任务，打印error日志，不会阻塞线程
            config.setHandler(new MqIgnoreRejectionPolicy());
            LOGGER.debug("MQ生产者线程池PoolName[{}]初始参数配置获取成功,CorePoolSize[{}],MaxPoolSize[{}],QueueDepth[{}],KeepAliveTime[{}]!", threadPoolName, coreSize, maxSize, queueCapacity, keepSeconds);
        } catch (Exception e) {
            // 如果配置参数错误，记录错误日志并抛出自定义异常
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "MQ生产者线程池[{}]初始参数配置错误,请检查配置文件配置信息!", threadPoolName);
            throw new FlowException(T_X0005091.getCode(), null, T_X0005091.getCodeMsg("应用MQ生产者线程池初始参数配置错误," + "请检查配置文件配置信息!", threadPoolName), e);
        }
        // 返回配置好的线程池配置对象
        return config;
    }

    *//**
     * 处理消息发送逻辑
     *
     * @param MqMessage 要发送的消息对象，包含消息内容和相关属性
     *//*
    protected void messageSend(MqMessage MqMessage) {
        // 获取消息主题
        String topic = getTopic();
        String messageId = MqMessage.getMsgId();
        try {
            // 获取消息队列开关配置
            String mqFlag = cacheClientApi.getSysCfg(MQ_OPENFLAG);
            boolean isSuccess;
            MqMessage.setTopic(topic);
            // 处理消息：消息平台正常且开关打开
            if ("1".equals(mqFlag)) {
                // 尝试将消息提交到分布式消息队列
                isSuccess = submitToDistributedMq(MqMessage);
                if (!isSuccess) {
                    // 如果分布式消息队列提交失败，则尝试降级本地处理
                    isSuccess = fallbackToLocal(MqMessage);
                }
            } else {
                // 如果消息平台开关未打开，直接降级本地处理
                isSuccess = fallbackToLocal(MqMessage);
            }
            // 如果所有尝试都失败了，将消息写入文件作为最后的备选方案
            if (!isSuccess) {
                LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "消息发送分布式消息平台失败,降级写本地文件,TOPIC[{}], 消息ID:[{}]", topic, messageId);
                fallbackToFileHandler.writeToFileTask(MqMessage);
            }
        } catch (Exception e) {
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "消息发送分布式消息平台异常,降级写本地文件,TOPIC[{}],消息ID[{}],异常信息[{}]", topic, messageId, e.getMessage(), e);
            // 避免抛出异常终止线程，将消息写入本地文件
            fallbackToFileHandler.writeToFileTask(MqMessage);
        }
    }

    *//**
     * 将消息提交到分布式消息队列
     * 此方法负责将给定的消息对象提交到分布式消息平台它设置了消息的主题，
     * 记录消息的详细信息，并尝试同步发送消息如果发送失败，它会记录错误信息
     *
     * @param MqMessage 要提交到分布式消息队列的消息对象
     * @return 如果消息成功提交，则返回true；否则返回false
     *//*
    protected boolean submitToDistributedMq(MqMessage MqMessage) {
        try {
            String topic = MqMessage.getTopic();
            Long startTime = Timer.getStartTime();
            LOGGER.info("消息发送分布式消息平台开始,TOPIC[{}],消息ID:[{}]", topic, MqMessage.getMsgId());
            // 尝试同步发送消息
            SendResult syncSend = MqTemplate.syncSend(MqMessage);
            // 检查消息是否成功发送
            boolean isSuccess = syncSend.getCode() == 0;
            if (isSuccess) {
                LOGGER.info("消息发送分布式消息平台成功,TOPIC[{}],消息ID:[{}],usedTime[{}]ms", topic, MqMessage.getMsgId(), Timer.getUsedTime(startTime));
            } else {
                LOGGER.error("消息发送分布式消息平台失败,消息平台返回码[{}],TOPIC[{}],消息ID:[{}],usedTime[{}]ms", syncSend.getCode(), topic, MqMessage.getMsgId(), Timer.getUsedTime(startTime));
            }
            // 返回消息发送结果
            return isSuccess;
        } catch (Exception e) {
            // 如果发生异常，记录错误信息
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "消息发送分布式消息平台异常,TOPIC[{}],消息ID[{}],异常信息:[{}]", MqMessage.getTopic(), MqMessage.getMsgId(), e.getMessage(), e);
            // 返回失败结果
            return false;
        }
    }*/


    /**
     * MessageTask类实现了Runnable接口，用于处理消息的发送任务
     * 它封装了一个MqMessage对象，并在run方法中执行消息发送操作
     */
    class MessageTask implements Runnable {
        // MqMessage对象，代表需要被发送的消息
        /*private final MqMessage MqMessage;

        MessageTask(MqMessage MqMessage) {
            this.MqMessage = MqMessage;
        }

        MqMessage getMessage() {
            return MqMessage;
        }*/

        /**
         * 实现Runnable接口的run方法，用于执行消息发送任务
         * 此方法被调用时，会触发消息的发送
         */
        @Override
        public void run() {
//            messageSend(MqMessage);
        }
    }
}



