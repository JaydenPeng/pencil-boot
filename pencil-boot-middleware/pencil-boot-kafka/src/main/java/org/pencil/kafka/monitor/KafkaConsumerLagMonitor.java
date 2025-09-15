package org.pencil.kafka.monitor;

import io.prometheus.client.Gauge;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Kafka消费者组延迟监控服务
 * 定期检查消费者组的消费进度，计算消费延迟并更新监控指标
 * 
 * @author pencil
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "pencil.kafka.monitor.lag.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConsumerLagMonitor {

    @Autowired
    private KafkaAdmin kafkaAdmin;
    
    @Autowired
    private Gauge kafkaLag;
    
    /**
     * 定期执行消费者组延迟监控
     * 默认每分钟执行一次
     */
    @Scheduled(fixedDelayString = "${pencil.kafka.monitor.lag.interval:60000}")
    public void monitorConsumerLag() {
        try {
            Properties props = new Properties();
            props.putAll(kafkaAdmin.getConfigurationProperties());
            
            try (AdminClient adminClient = AdminClient.create(props)) {
                // 获取所有消费者组
                ListConsumerGroupsResult groupsResult = adminClient.listConsumerGroups();
                for (ConsumerGroupListing group : groupsResult.all().get()) {
                    String groupId = group.groupId();
                    
                    // 获取消费者组的消费偏移量
                    ListConsumerGroupOffsetsResult offsetsResult = adminClient.listConsumerGroupOffsets(groupId);
                    Map<TopicPartition, OffsetAndMetadata> consumedOffsets = offsetsResult.partitionsToOffsetAndMetadata().get();
                    
                    // 获取主题分区的最新偏移量
                    Map<TopicPartition, Long> endOffsets = getLatestOffsets(props, consumedOffsets.keySet());
                    
                    // 计算并记录每个分区的消费延迟
                    for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : consumedOffsets.entrySet()) {
                        TopicPartition partition = entry.getKey();
                        long consumedOffset = entry.getValue().offset();
                        long latestOffset = endOffsets.getOrDefault(partition, 0L);
                        long lag = Math.max(0, latestOffset - consumedOffset);
                        
                        // 更新监控指标
                        kafkaLag.labels(
                                partition.topic(),
                                groupId,
                                String.valueOf(partition.partition())
                        ).set(lag);
                        
                        if (lag > 1000) {
                            log.warn("消费者组延迟较大: group={}, topic={}, partition={}, lag={}",
                                    groupId, partition.topic(), partition.partition(), lag);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("监控消费者组延迟异常", e);
        }
    }
    
    /**
     * 获取主题分区的最新偏移量
     */
    private Map<TopicPartition, Long> getLatestOffsets(Properties props, 
                                                      java.util.Set<TopicPartition> partitions) 
                                                      throws ExecutionException, InterruptedException {
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            return consumer.endOffsets(partitions);
        }
    }
}