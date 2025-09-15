package org.pencil.common.exception;

import lombok.Getter;

/**
 * 基础异常类，为所有自定义异常提供通用功能
 * 
 * @author pencil
 * @date 25/04/28 10:35
 */
@Getter
public abstract class BaseException extends RuntimeException {

    private final Integer code;
    private final String message;

    protected BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    protected BaseException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }
    
    protected BaseException(String message) {
        this(-500, message);
    }
}