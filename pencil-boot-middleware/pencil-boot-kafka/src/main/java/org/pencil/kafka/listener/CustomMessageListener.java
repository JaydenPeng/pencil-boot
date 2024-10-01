package org.pencil.kafka.listener;

import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.pencil.common.contexts.GlobalContext;
import org.slf4j.MDC;
import org.springframework.kafka.listener.MessageListener;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author pencil
 * @date 24/10/01 19:37
 */
@Slf4j
public class CustomMessageListener implements MessageListener<String, Object> {

    @Resource
    private Counter kafkaCounter;

    @Override
    public void onMessage(ConsumerRecord<String, Object> consumerRecord) {
        try {
            GlobalContext.generateTraceId("mq-" + UUID.randomUUID());
            if (log.isDebugEnabled()) {
                log.debug("kafka consumer record: {}", consumerRecord);
            }
            kafkaCounter.labels(consumerRecord.topic(), "success").inc();
        } finally {
            MDC.clear();
        }
    }
}
