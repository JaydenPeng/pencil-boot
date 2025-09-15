package org.pencil.redis.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import org.springframework.context.annotation.Bean;

/**
 * @author pencil
 * @date 24/10/11 20:24
 */
public class MetricsConfig {

    @Bean(name = "redisMsgCounter")
    public Counter redisMsgCounter(CollectorRegistry registry) {
        return Counter.build()
                .name("pencil_redis_msg_counter")
                .labelNames("topic", "group", "type")
                .help("pencil redis msg counter")
                .register(registry);
    }

}
