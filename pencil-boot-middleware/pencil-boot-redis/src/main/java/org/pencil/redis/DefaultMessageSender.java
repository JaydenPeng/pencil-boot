package org.pencil.redis;

import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;
import org.pencil.redis.utils.RedisTool;

import javax.annotation.Resource;

/**
 * @author pencil
 * @date 24/10/11 20:32
 */
@Slf4j
public class DefaultMessageSender implements MessageSender {

    @Resource
    private RedisTool redisTool;

    @Resource
    private Counter redisMsgCounter;

    @Override
    public void sendBroadcast(String topic, String message) {

        if (log.isDebugEnabled()) {
            log.debug("redis sendBroadcast topic:{}, message:{}", topic, message);
        }

        redisMsgCounter.labels(topic, "default", "sender").inc();
        redisTool.publishMessage(topic, message);
    }

    @Override
    public void sendMessage(String topic, String group, String message) {

        if (log.isDebugEnabled()) {
            log.debug("redis sendMessage topic:{}, group:{}, message:{}", topic, group, message);
        }

        redisMsgCounter.labels(topic, group, "sender").inc();
        redisTool.publishStream(topic, group, message);
    }
}
