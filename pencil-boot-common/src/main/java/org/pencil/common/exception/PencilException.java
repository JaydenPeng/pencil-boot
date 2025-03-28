package org.pencil.common.exception;

import lombok.Getter;

/**
 * @author pencil
 * @date 24/10/01 18:32
 */
@Getter
public class PencilException extends RuntimeException {

    private final Integer code;

    private final String message;

    private PencilException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public static PencilException of(Integer code, String message) {
        return new PencilException(code, message);
    }

    public static PencilException of(String message) {
        return new PencilException(-500, message);
    }

    public static PencilException of(ErrorCode code) {
        return new PencilException(code.getCode(), code.getMessage());
    }

}
