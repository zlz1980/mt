package com.n.fbsp.atom.platform.seed.other.atom.mq;

import com.n.fbsp.atom.platform.seed.other.atom.mq.common.AbstractMqMessageProducer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

@Component
public class SMSMqMessageProducer extends AbstractMqMessageProducer {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

    }
    /*private static final String S_FBSP_S_SAAAAAAA = "S-FBSP-S-SAAAAAAA";
    private static final String SEQ_LOG = "seq_Log";

    private static final Logger LOGGER = LoggerFactory.getLogger(SMSMqMessageProducer.class);
    private static final String CAL_UNIQUE_KEY = "calUniqueKey";
    private final MqHttpSvcClientImpl mqHttpSvcClient;
    @Autowired
    SequenceClient sequenceClient;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Value("${mq.producer.topicSms}")
    private String topic;

    @Value("${fbsp.smsThreadPool.corePoolSize}")
    private String corePoolSize;

    @Value("${fbsp.smsThreadPool.maxPoolSize}")
    private String maxPoolSize;

    @Value("${fbsp.smsThreadPool.queueCapacity}")
    private String queueCapacity;

    @Value("${fbsp.smsThreadPool.keepAliveSeconds}")
    private String keepAliveSeconds;

    public SMSMqMessageProducer(MqHttpSvcClientImpl mqHttpSvcClient) {
        this.mqHttpSvcClient = mqHttpSvcClient;
    }

    @Override
    protected boolean fallbackToLocal(MqMessage MqMessage) {
        Long startTime = Timer.getStartTime();
        LOGGER.info("消息降级,联机处理开始,TOPIC[{}],消息ID[{}]", MQ_TOPIC_SMS, MqMessage.getMessageId());
        LOGGER.debug("消息降级,联机处理内容[{}]", new String(MqMessage.getBody()));
        boolean fallbackResult;
        try {
            // 1.执行通信
            fallbackResult = doCommunication(MqMessage);
            if (fallbackResult) {
                LOGGER.info("消息降级,联机处理成功,TOPIC[{}],消息ID[{}],usedTime[{}]", MQ_TOPIC_SMS, MqMessage.getMessageId(), Timer.getUsedTime(startTime));
            } else {
                LOGGER.info("消息降级,联机处理失败,TOPIC[{}],消息ID[{}],usedTime[{}]", MQ_TOPIC_SMS, MqMessage.getMessageId(), Timer.getUsedTime(startTime));
                // 2.通信失败，降级写本地失败数据库
                fallbackResult = writeMqToDatabase(MqMessage);
            }
        } catch (Exception ex) {
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "消息降级,联机处理异常,TOPIC[{}],消息ID[{}],异常信息:[{}]", MQ_TOPIC_SMS, MqMessage.getMsgId(), ex.getMessage(), ex);
            // 3.其他异常,尝试写本地数据库
            fallbackResult = writeMqToDatabase(MqMessage);
        }
        return fallbackResult;
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
        threadPoolMap.put(MQ_THREAD_POOL_NAME_KEY, MQ_THREAD_TYPE_SMS);
        return super.getThreadPoolConfig(threadPoolMap);
    }

    @Override
    protected void initThreadPool(ThreadPoolConfig config) {
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(executor)) {
            this.executor = ThreadPoolExecutorFactory.createNewThreadPool(config);
            LOGGER.info("线程池配置信息[{}],TOPIC[{}],MQ生产者线程池初始化成功", config.toString(), getTopic());
        }
    }

    @Override
    protected String getTopic() {
        return topic;
    }


    *//**
     * 执行批量流程处理
     * 此方法用于根据业务流程ID调用不同的服务，并记录日志信息
     * 如果调用成功，将返回服务执行的结果；如果调用失败或信息缺失，将抛出异常，并记录错误信息
     *
     * @param MqMessage 包含批转联任务相关信息的Map，包含所有执行服务所需的参数和信息
     *//*
    private boolean doCommunication(MqMessage MqMessage) {
        HttpEntity<String> res;
        try {
            String jsonString = new String(MqMessage.getBody());
            Map<String, String> properties = MqMessage.getProperties();
            String chnlNo = properties.get(CHNL_NO);
            String fTranCode = properties.get(F_TRAN_CODE);
            Map<String, Object> messageBodyMap = JsonUtil.strToMap(jsonString);
            String calUniqueKey = messageBodyMap.get(CAL_UNIQUE_KEY).toString();
            //降级处理增加后缀，方便t_pb_log进行日志记录
            calUniqueKey = getNewCalUniqueKey(calUniqueKey);
            messageBodyMap.put(CAL_UNIQUE_KEY, calUniqueKey);
            // 执行服务调用，使用外部交易码和业务ID作为参数，并将流程对象转换为JSON字符串传递给服务执行方法
            res = mqHttpSvcClient.exec(chnlNo, fTranCode, messageBodyMap);
            if (!ObjectUtils.isEmpty(res)) {
                // 将响应体转换为Map对象
                Map<String, Object> response = JsonUtil.strToMap(res.getBody());
                LinkedHashMap<String, Object> head = (LinkedHashMap<String, Object>) response.get(HEAD);
                if (!ObjectUtils.isEmpty(head)) {
                    String returnCode = (String) head.get(HEAD_RETURNCODE);
                    return returnCode.equals(S_FBSP_S_SAAAAAAA);
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("消息降级,联机处理通信异常,TOPIC[{}],消息ID[{}],异常信息:[{}]", MQ_TOPIC_SMS, MqMessage.getMsgId(), e.getMessage(), e);
            return false;
        }
        return true;
    }

    *//**
     * 将消息队列消息降级写入数据库
     * 当消息处理失败时，通过此方法将消息持久化到数据库中，以保证消息不丢失
     *
     * @param MqMessage 消息队列消息对象，包含消息体和消息属性
     *//*
    private boolean writeMqToDatabase(MqMessage MqMessage) {
        boolean writeDbResult;
        try {
            // 记录开始时间，用于后续计算处理时长
            Long startTime = Timer.getStartTime();
            // 记录消息降级开始日志，包括主题和消息ID
            LOGGER.info("消息降级,写数据库开始,TOPIC[{}],消息ID[{}]", MQ_TOPIC_SMS, MqMessage.getMsgId());
            // 将消息体转换为字符串，以便存储
            String jsonString = new String(MqMessage.getBody());
            // 获取InitMqFailsMapper实例，用于操作数据库
            InitMqFailsMapper initMqFailsMapper = sqlSessionTemplate.getMapper(InitMqFailsMapper.class);
            // 创建MqFailMessage对象，用于存储消息到数据库
            MqFailMessage mqFailMessage = new MqFailMessage();
            // 获取消息属性，用于设置MqFailMessage对象的属性
            Map<String, String> properties = MqMessage.getProperties();
            String chnlNo = properties.get(CHNL_NO);
            String fTranCode = properties.get(F_TRAN_CODE);
            // 设置MqFailMessage对象的属性，包括消息ID、消息体、状态、主题、渠道号和交易码
            mqFailMessage.setMqId(MqMessage.getMsgId());
            mqFailMessage.setMqMessage(jsonString);
            mqFailMessage.setStatus("B");
            mqFailMessage.setTopic(MQ_TOPIC_SMS);
            mqFailMessage.setChnlNo(chnlNo);
            mqFailMessage.setfTranCode(fTranCode);
            // 插入消息到数据库
            initMqFailsMapper.insertMqFailsMessage(mqFailMessage);
            writeDbResult = true;
            // 记录消息降级成功日志，包括主题、消息ID和处理时长
            LOGGER.info("消息降级,写数据库成功,TOPIC[{}],消息ID[{}],usedTime[{}]", MQ_TOPIC_SMS, MqMessage.getMsgId(), Timer.getUsedTime(startTime));
        } catch (Exception e) {
            // 记录消息降级写数据库异常日志，包括主题、消息ID和异常信息
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "消息降级,写数据库异常,TOPIC[{}],消息ID[{}],异常信息:[{}]", MQ_TOPIC_SMS, MqMessage.getMsgId(), e.getMessage(), e);
            writeDbResult = false;
        }
        return writeDbResult;
    }

    @NotNull
    private String getNewCalUniqueKey(String calUniqueKey) {
        Long sequenceCode;
        try {
            sequenceCode = sequenceClient.nextId(SEQ_LOG, 0);
            String sequenceNo = sequenceCode.toString();
            //判断sequenceCode长度，超过6位截取后6位，不足6位补齐6位
            if (sequenceNo.length() > 6) {
                sequenceNo = sequenceNo.substring(sequenceNo.length() - 6);
            } else {
                sequenceNo = String.format("%06d", sequenceCode);
            }
            calUniqueKey = calUniqueKey.concat("-").concat(sequenceNo);
        } catch (Exception e) {
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "生成序列异常,异常信息[{}]", e.getMessage(), e);
            //降级拼接时间戳
            calUniqueKey = calUniqueKey.concat("-").concat(String.valueOf(System.currentTimeMillis()));
        }
        return calUniqueKey;
    }*/

}
