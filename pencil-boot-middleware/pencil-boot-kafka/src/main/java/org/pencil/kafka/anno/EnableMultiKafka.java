package org.pencil.kafka.anno;

import org.pencil.kafka.config.EnableMultiKafkaConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pencil
 * @date 24/10/01 19:36
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableMultiKafkaConfig.class)
public @interface EnableMultiKafka {
}
