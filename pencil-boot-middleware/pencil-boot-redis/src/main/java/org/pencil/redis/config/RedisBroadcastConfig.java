package org.pencil.redis.config;

import io.prometheus.client.Counter;
import org.pencil.redis.BroadcastListener;
import org.pencil.redis.proxy.BroadcastListenerProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author pencil
 * @date 24/10/11 20:26
 */
public class RedisBroadcastConfig {

    @Resource
    private Counter redisMsgCounter;

    @Resource
    private ApplicationContext applicationContext;

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        String[] listenerBeans = applicationContext.getBeanNamesForType(BroadcastListener.class);

        Arrays.stream(listenerBeans).forEach(listenerBean -> {
                    BroadcastListener listener = applicationContext.getBean(listenerBean, BroadcastListener.class);
                    BroadcastListenerProxy proxy = new BroadcastListenerProxy(listener, redisMsgCounter);
                    container.addMessageListener(proxy, new ChannelTopic(proxy.topic()));
                });
        return container;
    }

}
