package org.pencil.kafka.config;

import org.pencil.kafka.listener.CustomMessageListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

/**
 * @author pencil
 * @date 24/10/01 19:47
 */
public class SingleKafkaConfig {

    @Bean
    @ConditionalOnMissingBean(EnableMultiKafkaConfig.class)
    public ConcurrentKafkaListenerContainerFactory<String, Object> containerFactory(ConsumerFactory<String, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setContainerCustomizer(container -> container.getContainerProperties().setMessageListener(new CustomMessageListener()));
        return factory;
    }
}