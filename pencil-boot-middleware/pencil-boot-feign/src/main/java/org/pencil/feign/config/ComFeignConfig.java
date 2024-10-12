package org.pencil.feign.config;

import feign.Logger;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author pencil
 * @date 24/10/01 19:00
 */
public class ComFeignConfig {

    @Resource
    private Counter feignRequestCounter;

    @Resource
    private Histogram feignRequesHistogram;

    @Bean
    public Logger.Level getLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Logger getLogger() {
        return new CustomFeignLogger(feignRequestCounter, feignRequesHistogram);
    }
}
