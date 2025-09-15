package org.pencil.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

/**
 * @author pencil
 * @date 24/10/01 19:34
 */
public class EnableMultiKafkaConfig {

    @Bean
    public KafkaDynamicListenerConfig kafkaDynamicConfig(MultiKafkaProperties properties) {
        return new KafkaDynamicListenerConfig(properties);
    }

    @Bean
    public MultiKafkaManager multiKafkaManager(Map<String, KafkaTemplate<String, Object>> kafkaTemplates) {
        return new MultiKafkaManager(kafkaTemplates);
    }

}