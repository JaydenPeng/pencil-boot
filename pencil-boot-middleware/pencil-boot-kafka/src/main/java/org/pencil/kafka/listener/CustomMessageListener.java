package org.pencil.kafka.listener;

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.pencil.common.contexts.GlobalContext;
import org.slf4j.MDC;
import org.springframework.kafka.listener.MessageListener;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 自定义Kafka消息监听器
 * 处理消息接收并记录相关监控指标
 * 
 * @author pencil
 * @date 24/10/01 19:37
 */
@Slf4j
public class CustomMessageListener implements MessageListener<String, Object> {

    @Resource
    private Counter kafkaCounter;
    
    @Resource
    private Histogram kafkaLatency;

    @Override
    public void onMessage(ConsumerRecord<String, Object> consumerRecord) {
        long startTime = System.currentTimeMillis();
        String traceId = "mq-" + UUID.randomUUID();
        
        try {
            GlobalContext.generateTraceId(traceId);
            MDC.put("topic", consumerRecord.topic());
            MDC.put("partition", String.valueOf(consumerRecord.partition()));
            MDC.put("offset", String.valueOf(consumerRecord.offset()));
            
            if (log.isDebugEnabled()) {
                log.debug("接收Kafka消息: topic={}, partition={}, offset={}, key={}",
                        consumerRecord.topic(), consumerRecord.partition(), 
                        consumerRecord.offset(), consumerRecord.key());
            }
            
            // 记录消息接收成功
            kafkaCounter.labels(consumerRecord.topic(), "success", "receive").inc();
            
        } catch (Exception e) {
            // 记录消息处理失败
            kafkaCounter.labels(consumerRecord.topic(), "error", "receive").inc();
            log.error("处理Kafka消息异常: topic={}, partition={}, offset={}", 
                    consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset(), e);
        } finally {
            // 记录处理延迟
            long processingTime = System.currentTimeMillis() - startTime;
            kafkaLatency.labels(consumerRecord.topic(), "receive").observe(processingTime);
            
            MDC.clear();
            GlobalContext.clearAll();
        }
    }
}
