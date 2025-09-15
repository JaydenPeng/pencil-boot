package org.pencil.redis.proxy;

import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;
import org.pencil.common.contexts.GlobalContext;
import org.pencil.redis.BroadcastListener;
import org.springframework.data.redis.connection.Message;

import java.util.UUID;

/**
 * @author pencil
 * @date 24/10/11 20:46
 */
@Slf4j
public class BroadcastListenerProxy implements BroadcastListener {

    private final BroadcastListener delegate;

    private final Counter redisMsgCounter;

    public BroadcastListenerProxy(BroadcastListener delegate, Counter redisMsgCounter) {
        this.delegate = delegate;
        this.redisMsgCounter = redisMsgCounter;
    }


    @Override
    public String topic() {
        return delegate.topic();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        GlobalContext.generateTraceId("redis-" + topic() + UUID.randomUUID());
        if (log.isDebugEnabled()) {
            log.debug("redis topic: {}, message: {}", topic(), message.toString());
            redisMsgCounter.labels(topic(), "default", "received").inc();
            delegate.onMessage(message, pattern);
        }
    }
}
