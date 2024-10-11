package org.pencil.redis.proxy;

import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;
import org.pencil.common.contexts.GlobalContext;
import org.pencil.redis.ConsumerListener;
import org.pencil.redis.utils.RedisTool;
import org.springframework.data.redis.connection.stream.ObjectRecord;

import java.util.UUID;

/**
 * @author pencil
 * @date 24/10/11 20:49
 */
@Slf4j
public class ConsumerListenerProxy implements ConsumerListener {

    private final ConsumerListener delegate;

    private final RedisTool redisTool;

    private final Counter redisMsgCounter;

    public ConsumerListenerProxy(ConsumerListener delegate, RedisTool redisTool, Counter redisMsgCounter) {
        this.delegate = delegate;
        this.redisTool = redisTool;
        this.redisMsgCounter = redisMsgCounter;
    }

    @Override
    public String topic() {
        return delegate.topic();
    }

    @Override
    public String group() {
        return delegate.group();
    }

    @Override
    public void onMessage(ObjectRecord<String, String> message) {

        GlobalContext.generateTraceId("redis-" + topic() + UUID.randomUUID());
        if (log.isDebugEnabled()) {
            log.debug("consumer receive message, topic={}, group={}, message={}", topic(), group(), message);
        }
        try {
            redisMsgCounter.labels(topic(), group(), "received").inc();
            delegate.onMessage(message);
        } finally {
            redisTool.getTemplate().opsForStream().acknowledge(topic(), group(), message.getId());
        }
    }
}
