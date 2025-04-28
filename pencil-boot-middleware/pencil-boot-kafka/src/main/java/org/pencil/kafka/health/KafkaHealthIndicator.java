package org.pencil.kafka.health;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Kafka健康检查指示器
 * 用于Spring Boot Actuator健康检查，检测Kafka集群连接状态
 * 
 * @author pencil
 */
@Slf4j
@Component
@ConditionalOnClass(name = "org.springframework.boot.actuate.health.HealthIndicator")
public class KafkaHealthIndicator extends AbstractHealthIndicator {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try {
            Properties props = new Properties();
            props.putAll(kafkaAdmin.getConfigurationProperties());
            
            try (AdminClient adminClient = AdminClient.create(props)) {
                // 获取集群信息
                DescribeClusterResult clusterResult = adminClient.describeCluster();
                
                // 设置超时时间
                int nodeCount = clusterResult.nodes().get(10, TimeUnit.SECONDS).size();
                String clusterId = clusterResult.clusterId().get(10, TimeUnit.SECONDS);
                
                // 构建健康信息
                builder.up()
                       .withDetail("clusterId", clusterId)
                       .withDetail("nodeCount", nodeCount);
            }
        } catch (Exception e) {
            log.warn("Kafka健康检查失败", e);
            builder.down().withException(e);
        }
    }
}