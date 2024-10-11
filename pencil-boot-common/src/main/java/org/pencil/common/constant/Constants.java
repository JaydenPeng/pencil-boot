package org.pencil.common.constant;

import java.util.Optional;

/**
 * @author pencil
 * @date 24/10/11 21:26
 */
public class Constants {

    public static final String TRACE_ID = "trace_id";

    public static final String USER_ID = "userid";

    public static final String NULL = "null";

    public static String convertMdc(String value) {
        return Optional.ofNullable(value).map(str -> "[" + str + "] ").orElse("");
    }

    private Constants() {
    }
}
