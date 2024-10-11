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


    private StreamMessageListenerContainer<String, ObjectRecord<String, String>> createListenerContainer(ConsumerListener listener) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .pollTimeout(Duration.ofSeconds(2))
                        .batchSize(10)
                        .targetType(String.class)
                        .build();

        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container =
                StreamMessageListenerContainer.create(connectionFactory, options);

        StreamOffset<String> offset = StreamOffset.create(listener.topic(), ReadOffset.lastConsumed());
        Consumer consumer = Consumer.from(listener.group(), "consumer-" + UUID.randomUUID());

        container.register(StreamMessageListenerContainer.StreamReadRequest.builder(offset)
                .consumer(consumer)
                .autoAcknowledge(false).build(),
                new ConsumerListenerProxy(listener, redisTool, redisMsgCounter));

        return container;
    }
}
