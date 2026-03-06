/*
package com.n.fbsp.atom.platform.seed.other.atom.mq.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_MQ_PRODUCER_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.CHNL_NO;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.F_TRAN_CODE;
import static com.nantian.nbp.utils.Constants.PARAM_SPILT_FLAG;

*/
/**
 * @author
 * @date 2025/3/17
 *//*

@Component
public class FallbackToFileHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FallbackToFileHandler.class);
    // 日志文件后缀
    private static final String FILE_EXTENSION = ".log";

    private final ReentrantLock writeLock = new ReentrantLock();

    @Value("${PB_MQ_FILE_PATH:/cloud/nas/mq/pb/}")
    private String mqFliePath;
    @Value("${MY_POD_NAME}")
    private String myPodName;
    @Value("${mq.producer.topicSms}")
    private String smsTopic;

    */
/**
     * 将消息写入文件的任务方法
     * 该方法负责将接收到的消息格式化后，根据消息的主题写入到对应的文件中
     *
     * @param message 要写入文件的消息对象，包含主题和内容
     *//*

    protected void writeToFileTask(Message message) {
        // 写入原消息体，不做格式转换
        String msgBody = new String(message.getBody(), StandardCharsets.UTF_8);
        if (StringUtils.hasText(msgBody)) {
            // 获取消息的主题，用于确定消息应写入的文件
            String topic = message.getTopic();
            //针对短信类的消息，增加mqId,chnlNo,fTranCode的登记，方便写入T_PB_MQBATCH_FAILS表中
            if (smsTopic.equals(topic)) {
                Map<String, String> properties = message.getProperties();
                String chnlNo = properties.get(CHNL_NO);
                String fTranCode = properties.get(F_TRAN_CODE);
                String mqId = message.getKeys();
                //把chnlNo,fTranCode,mqId追加到msgBody中
                msgBody = msgBody + PARAM_SPILT_FLAG + chnlNo + PARAM_SPILT_FLAG + fTranCode + PARAM_SPILT_FLAG + mqId;
            }
            msgBody = msgBody.replaceAll("[\r\n\f]", "");
            // 将格式化后的消息写入到对应主题的文件中
            writeToFile(msgBody, topic);
            return;
        }
        LOGGER.warn("TOPIC:[{}] 消息ID[{}]的消息体为空, 降级写文件策略不写入该空消息体", message.getTopic(), message.getMessageId());
    }

    */
/**
     * 将日志条目写入指定主题的文件中
     * 此方法处理文件路径的生成、文件的创建（包括必要的父目录）、
     * 以及通过文件锁确保并发安全的日志条目追加操作
     *
     * @param logEntry 要写入文件的日志条目
     * @param topic    日志的主题，用于确定文件路径
     *//*

    private void writeToFile(String logEntry, String topic) {
        // 根据主题获取日志文件路径
        String filePath = getLogFilePath(topic);
        try {
            writeLock.lock();
            File file = new File(filePath);
            // 确保父目录存在
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                // 递归创建目录
                parentDir.mkdirs();
            }
        } catch (Exception e) {
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "准备MQ信息降级本地文件目录失败{}", e.getMessage(), e);
        } finally {
            writeLock.unlock();
        }
        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            writeLock.lock();
            fileWriter.write(logEntry + "\n");
        } catch (IOException e) {
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "MQ信息降级写入本地文件异常,异常信息[{}]", e.getMessage(), e);
        } finally {
            writeLock.unlock();
        }
    }

    */
/**
     * 获取日志文件路径
     * 该方法根据主题生成一个唯一的日志文件路径，确保不同主题或不同日期的日志文件不会冲突
     * 使用当前日期和Pod名称作为文件名的一部分，进一步确保文件的唯一性
     *
     * @param topic 日志主题，用于区分不同类型的日志
     * @return 返回生成的日志文件路径
     *//*

    private String getLogFilePath(String topic) {
        // 获取当前日期字符串，格式为YYYYMMDD
        String date = DateUtil.getDayStr();
        // 返回文件路径，包含主题作为文件名的一部分，避免文件冲突
        return mqFliePath + topic + "_" + date + "_" + myPodName + FILE_EXTENSION;
    }

}
*/
