package org.pencil.redis.anno;

import org.pencil.redis.config.RedisPubConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pencil
 * @date 24/10/11 21:05
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(RedisPubConfig.class)
public @interface EnableRedisQueue {
}
