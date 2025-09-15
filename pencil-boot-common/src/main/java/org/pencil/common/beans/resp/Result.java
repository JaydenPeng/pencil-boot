package org.pencil.common.beans.resp;

import lombok.Data;

/**
 * @author pencil
 * @date 24/10/01 18:50
 */
@Data
public class Result<T> {

    private int code;

    private String message;

    private Object detail;

    private T data;

    private Result(int code, String message, Object detail, T data) {
        this.code = code;
        this.message = message;
        this.detail = detail;
        this.data = data;
    }

    public static <T> Result<T> of(int code, String message) {
        return new Result<>(code, message, null, null);
    }

    public static <T> Result<T> of(int code, String message, Object detail) {
        return new Result<>(code, message, detail, null);
    }

    public static <T> Result<T> of(String message, Object detail) {
        return new Result<>(-500, message, detail, null);
    }

    public static <T> Result<T> of(String message) {
        return new Result<>(-500, message, null, null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(0, "success", null, data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(0, "success", null, null);
    }

}
