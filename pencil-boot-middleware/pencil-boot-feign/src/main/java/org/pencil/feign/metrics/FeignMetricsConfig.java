package org.pencil.feign.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pencil
 * @date 24/10/12 10:49
 */
@Configuration
public class FeignMetricsConfig {

    @Bean
    public Counter feignRequestCounter(CollectorRegistry registry) {
        return Counter.build()
                .name("pencil_feign_request")
                .help("feign request counter")
                .labelNames("url", "status")
                .register(registry);
    }

    @Bean
    public Histogram feignRequestHistogram(CollectorRegistry registry) {
        return Histogram.build()
                .name("pencil_feign_request_time")
                .help("feign request time")
                .labelNames("url", "status")
                .register(registry);
    }
}
