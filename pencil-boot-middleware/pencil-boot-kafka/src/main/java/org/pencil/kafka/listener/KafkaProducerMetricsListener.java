package org.pencil.kafka.listener;

import cn.hutool.core.text.CharSequenceUtil;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

import javax.annotation.Resource;

/**
 * Kafka生产者指标监听器
 * 监控消息发送状态并记录相关指标
 * 
 * @author pencil
 * @date 24/10/01 19:39
 */
@Slf4j
public class KafkaProducerMetricsListener implements ProducerListener<String, Object> {

    @Resource
    private Counter kafkaCounter;
    
    @Resource
    private Histogram kafkaLatency;

    @Override
    public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
        // 计算发送延迟
        long sendTime = System.currentTimeMillis() - recordMetadata.timestamp();
        kafkaLatency.labels(producerRecord.topic(), "send").observe(sendTime);
        
        if (log.isDebugEnabled()) {
            log.debug("Kafka消息发送成功: topic={}, partition={}, offset={}, key={}",
                    recordMetadata.topic(), recordMetadata.partition(), 
                    recordMetadata.offset(), producerRecord.key());
        }

        kafkaCounter.labels(producerRecord.topic(), "success", "send").inc();
    }

    @Override
    public void onError(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("Kafka消息发送失败: topic={}, partition={}, key={}, value={}",
                recordMetadata != null ? recordMetadata.topic() : producerRecord.topic(),
                producerRecord.partition(),
                producerRecord.key(),
                CharSequenceUtil.sub(String.valueOf(producerRecord.value()), 0, 1024),
                exception);
                
        kafkaCounter.labels(producerRecord.topic(), "fail", "send").inc();
    }
}
