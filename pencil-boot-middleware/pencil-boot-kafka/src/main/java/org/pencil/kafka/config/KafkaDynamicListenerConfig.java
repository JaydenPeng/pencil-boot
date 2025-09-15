package org.pencil.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pencil
 * @date 24/10/01 19:11
 */
public class KafkaDynamicListenerConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final MultiKafkaProperties properties;

    public KafkaDynamicListenerConfig(MultiKafkaProperties properties) {
        this.properties = properties;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        registerFactories();
    }

    @Bean
    public Map<String, KafkaTemplate<String, Object>> kafkaTemplates(MultiKafkaProperties properties) {
        Map<String, KafkaTemplate<String, Object>> kafkaTemplates = new HashMap<>(properties.getDataSources().size());

        properties.getDataSources().forEach((name, config)-> {
            Map<String, Object> configProps = new HashMap<>(config.buildProducerProperties());
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getProducer().getKeySerializer());
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getProducer().getValueSerializer());

            DefaultKafkaProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
            kafkaTemplates.put(name, new KafkaTemplate<>(producerFactory));
        });
        return kafkaTemplates;
    }

    @Bean
    public ConsumerFactory<String, Object> createConsumerFactory(KafkaProperties properties) {
        Map<String, Object> configProps = new HashMap<>(properties.buildConsumerProperties());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, properties.getConsumer().getKeyDeserializer());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, properties.getConsumer().getValueDeserializer());
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    private void registerFactories() {
        properties.getDataSources().forEach((name, config) -> {
            ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(createConsumerFactory(config));
            ((DefaultListableBeanFactory)(applicationContext.getAutowireCapableBeanFactory()))
                    .registerSingleton(name + "KafkaFactory", factory);
        });
    }

}
