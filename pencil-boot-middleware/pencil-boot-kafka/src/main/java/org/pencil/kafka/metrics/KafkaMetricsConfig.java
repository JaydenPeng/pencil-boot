package org.pencil.kafka.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka指标配置类
 * 提供Kafka相关的监控指标定义，包括消息计数器、延迟直方图和消费者组监控等
 * 
 * @author pencil
 * @date 24/10/01 19:44
 */
@Configuration
public class KafkaMetricsConfig {

    @Bean
    public Counter kafkaCounter(CollectorRegistry registry) {
        return Counter.build()
                .labelNames("topic", "status", "operation")
                .name("pencil_kafka_counter")
                .help("Kafka消息计数器，统计消息发送和接收的数量")
                .register(registry);
    }
    
    @Bean
    public Histogram kafkaLatency(CollectorRegistry registry) {
        return Histogram.build()
                .labelNames("topic", "operation")
                .name("pencil_kafka_latency")
                .help("Kafka操作延迟(毫秒)")
                .buckets(1, 5, 10, 25, 50, 100, 250, 500, 1000, 2500, 5000)
                .register(registry);
    }
    
    @Bean
    public Gauge kafkaLag(CollectorRegistry registry) {
        return Gauge.build()
                .labelNames("topic", "group", "partition")
                .name("pencil_kafka_consumer_lag")
                .help("Kafka消费者组延迟(消息数)")
                .register(registry);
    }
}
