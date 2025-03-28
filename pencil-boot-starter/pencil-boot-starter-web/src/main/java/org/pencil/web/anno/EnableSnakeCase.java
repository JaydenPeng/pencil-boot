package org.pencil.web.anno;

import org.pencil.web.config.SnakeCaseConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pencil
 * @date 25/03/28 14:38
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(SnakeCaseConfig.class)
public @interface EnableSnakeCase {
}
