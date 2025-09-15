package org.pencil.redis.config;

import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;
import org.pencil.redis.ConsumerListener;
import org.pencil.redis.proxy.ConsumerListenerProxy;
import org.pencil.redis.utils.RedisTool;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * @author pencil
 * @date 24/10/11 20:57
 */
@Slf4j
public class RedisPubConfig {

    @Resource
    private RedisConnectionFactory connectionFactory;

    @Resource
    private List<ConsumerListener> listeners;

    @Resource
    private RedisTool redisTool;

    @Resource
    private Counter redisMsgCounter;

    @Bean
    public void registerListeners() {
        listeners.forEach(listener -> {
            StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = createListenerContainer(listener);
            container.start();
        });
    }

    /**
     * 创建一个监听容器，用于监听和处理消息流中的消息
     * 该方法配置并初始化一个StreamMessageListenerContainer实例，用于消费特定主题的消息
     * 它通过设置监听器选项，创建消费者组，并注册监听器来完成这一过程
     *
     * @param listener 消费者监听器，用于处理接收到的消息
     * @return 返回配置好的StreamMessageListenerContainer实例
     */
    private StreamMessageListenerContainer<String, ObjectRecord<String, String>> createListenerContainer(ConsumerListener listener) {
        // 配置监听器容器的选项，包括轮询超时、批处理大小和目标类型
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .pollTimeout(Duration.ofSeconds(2))
                        .batchSize(10)
                        .targetType(String.class)
                        .build();

        // 创建监听器容器实例
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container =
                StreamMessageListenerContainer.create(connectionFactory, options);

        // 创建流偏移量，指定从最后一个消费的位置开始读取消息
        StreamOffset<String> offset = StreamOffset.create(listener.topic(), ReadOffset.lastConsumed());
        // 生成一个唯一的消费者标识
        Consumer consumer = Consumer.from(listener.group(), "consumer-" + UUID.randomUUID());

        // 注册读取请求和自定义的消费者监听器代理
        container.register(StreamMessageListenerContainer.StreamReadRequest.builder(offset)
                        .consumer(consumer)
                        .autoAcknowledge(false).build(),
                new ConsumerListenerProxy(listener, redisTool, redisMsgCounter));

        // 返回配置好的监听器容器实例
        return container;
    }

}
