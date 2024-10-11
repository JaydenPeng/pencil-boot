package org.pencil.web.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pencil
 * @date 24/10/11 21:08
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReqStatistics {

    String value();

    boolean reqLog() default false;

    boolean respLog() default false;

    boolean costTime() default false;

    boolean metric() default false;

}
