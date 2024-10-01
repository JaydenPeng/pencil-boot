package org.pencil.kafka.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pencil
 * @date 24/10/01 19:44
 */
@Configuration
public class KafkaMetricsConfig {

    @Bean
    public Counter kafkaCounter(CollectorRegistry registry) {
        return Counter.build()
                .labelNames("topic", "status")
                .name("pencil_kafka_counter")
                .help("kafka counter")
                .register(registry);
    }

}
