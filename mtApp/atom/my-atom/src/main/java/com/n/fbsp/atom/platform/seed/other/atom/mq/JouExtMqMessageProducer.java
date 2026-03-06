package com.n.fbsp.atom.platform.seed.other.atom.mq;

import com.n.fbsp.atom.platform.seed.other.atom.mq.common.AbstractMqMessageProducer;
import com.n.fbsp.atom.platform.seed.other.service.IJournalExtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;

//@Component
public class JouExtMqMessageProducer extends AbstractMqMessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JouExtMqMessageProducer.class);
    @Autowired
    private IJournalExtService iJournalExtService;
    @Value("${mq.producer.topicJouExt}")
    private String topic;

    @Value("${fbsp.jouThreadPool.corePoolSize}")
    private String corePoolSize;

    @Value("${fbsp.jouThreadPool.maxPoolSize}")
    private String maxPoolSize;

    @Value("${fbsp.jouThreadPool.queueCapacity}")
    private String queueCapacity;

    @Value("${fbsp.jouThreadPool.keepAliveSeconds}")
    private String keepAliveSeconds;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

    }

    /*@Override
    protected boolean fallbackToLocal(MqMessage MqMessage) {
        try {
            Long startTime = Timer.getStartTime();
            LOGGER.info("消息降级,写数据库开始,TOPIC[{}],消息ID[{}]", MQ_TOPIC_JOUEXT, MqMessage.getMsgId());
            String jsonString = new String(MqMessage.getBody());
            JournalExt journalExt = JsonUtil.strToObj(jsonString, JournalExt.class);
            LOGGER.debug("消息降级,写数据库内容[{}]", journalExt);
            iJournalExtService.insertJournalExt(journalExt);
            // 降级处理成功
            LOGGER.info("消息降级,写数据库成功,TOPIC[{}],消息ID[{}],usedTime[{}]ms", MQ_TOPIC_JOUEXT, MqMessage.getMsgId(), Timer.getUsedTime(startTime));
            return true;
        } catch (Exception e) {
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "消息降级,写数据库异常,TOPIC[{}],消息ID[{}],异常信息:[{}]", MQ_TOPIC_JOUEXT, MqMessage.getMsgId(), e.getMessage(), e);
            return false;
        }
    }

    @Override
    protected ThreadPoolConfig getThreadPoolConfig() {
        HashMap<String, Object> threadPoolMap = new HashMap<>();
        // 线程池允许的最大线程数
        threadPoolMap.put(MQ_THREAD_MAX_POOL_SIZE, maxPoolSize);
        // 线程池核心数
        threadPoolMap.put(MQ_THREAD_CORE_POOL_SIZE, corePoolSize);
        // 线程池任务队列容量
        threadPoolMap.put(MQ_THREAD_QUEUE_CAPACITY, queueCapacity);
        // 非核心线程空闲时间
        threadPoolMap.put(MQ_THREAD_KEEP_ALIVE_SECONDS, keepAliveSeconds);
        // 线程池名称
        threadPoolMap.put(MQ_THREAD_POOL_NAME_KEY, MQ_THREAD_TYPE_JOU);
        return super.getThreadPoolConfig(threadPoolMap);
    }

    @Override
    protected void initThreadPool(ThreadPoolConfig config) {
        if (ObjectUtils.isEmpty(executor)) {
            this.executor = ThreadPoolExecutorFactory.createNewThreadPool(config);
            LOGGER.info("线程池配置信息[{}],TOPIC[{}],MQ生产者线程池初始化成功", config.toString(), getTopic());
        }
    }

    @Override
    protected String getTopic() {
        return topic;
    }*/

}
