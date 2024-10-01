package org.pencil.kafka.listener;

import cn.hutool.core.text.CharSequenceUtil;
import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

import javax.annotation.Resource;

/**
 * @author pencil
 * @date 24/10/01 19:39
 */
@Slf4j
public class KafkaProducerMetricsListener implements ProducerListener<String, Object> {

    @Resource
    private Counter kafkaCounter;

    @Override
    public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
        if (log.isDebugEnabled()) {
            log.debug("kafka send success, topic: {}, partition: {}, offset: {}, value:{}", recordMetadata.topic(),
                    recordMetadata.partition(), recordMetadata.offset(),
                    CharSequenceUtil.sub(producerRecord.value().toString(), 0, 1024));
        }

        kafkaCounter.labels(producerRecord.topic(), "success").inc();
    }

    @Override
    public void onError(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("kafka send error, topic: {}, partition: {}, offset: {}, value:{}", recordMetadata.topic(),
                producerRecord.partition(), recordMetadata.offset(),
                CharSequenceUtil.sub(producerRecord.value().toString(), 0, 1024), exception);
        kafkaCounter.labels(producerRecord.topic(), "fail").inc();
    }
}
