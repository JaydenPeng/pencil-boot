package org.pencil.kafka.config;

import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pencil
 * @date 24/10/01 19:32
 */
public class MultiKafkaManager {


    private final Map<String, KafkaTemplate<String, Object>> kafkaTemplates = new HashMap<>();

    public MultiKafkaManager(Map<String, KafkaTemplate<String, Object>> kafkaTemplates) {
        this.kafkaTemplates.putAll(kafkaTemplates);
    }

    public KafkaTemplate<String, Object> getKafkaTemplate(String name) {
        return kafkaTemplates.get(name);
    }

}
