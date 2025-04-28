package org.pencil.common.exception;

import lombok.Getter;

/**
 * @author pencil
 * @date 24/10/01 18:32
 */
@Getter
public class ServerException extends RuntimeException {

    private final Integer code;

    private final String message;

    private ServerException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public static ServerException of(Integer code, String message) {
        return new ServerException(code, message);
    }

    public static ServerException of(String message) {
        return new ServerException(-500, message);
    }

    public static ServerException of(ErrorCode code) {
        return new ServerException(code.getCode(), code.getMessage());
    }

}
