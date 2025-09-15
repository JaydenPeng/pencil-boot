package org.pencil.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka监控配置属性类
 * 
 * @author pencil
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "pencil.kafka.monitor")
public class KafkaMonitorProperties {
    
    /**
     * 是否启用消费者组延迟监控
     */
    private boolean lagEnabled = false;
    
    /**
     * 消费者组延迟监控间隔(毫秒)
     */
    private int lagInterval = 60000;
    
    /**
     * 是否启用JMX指标收集
     */
    private boolean jmxEnabled = false;
    
    /**
     * JMX指标收集间隔(毫秒)
     */
    private int jmxInterval = 60000;
}