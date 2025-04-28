package org.pencil.common.exception;

import lombok.Getter;

/**
 * @author pencil
 * @date 25/04/28 10:35
 */
@Getter
public class RequestException extends RuntimeException {

    private final Integer code;

    private final String message;

    private RequestException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public static RequestException of(Integer code, String message) {
        return new RequestException(code, message);
    }

    public static RequestException of(String message) {
        return new RequestException(-500, message);
    }

    public static RequestException of(ErrorCode code) {
        return new RequestException(code.getCode(), code.getMessage());
    }

}
