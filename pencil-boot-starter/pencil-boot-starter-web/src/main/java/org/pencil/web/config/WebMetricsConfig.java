package org.pencil.web.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pencil
 * @date 24/10/11 21:22
 */
@Configuration
public class WebMetricsConfig {

    @Bean
    public Counter requestCounter(CollectorRegistry registry) {
        return Counter.build()
                .name("pencil_http_request_counter")
                .labelNames("name", "status")
                .help("http request")
                .register(registry);
    }

    @Bean
    public Histogram requestHistogram(CollectorRegistry registry) {
        return Histogram.build()
                .name("pencil_http_request_histogram")
                .labelNames("name", "status")
                .help("http request")
                .register(registry);
    }
}
