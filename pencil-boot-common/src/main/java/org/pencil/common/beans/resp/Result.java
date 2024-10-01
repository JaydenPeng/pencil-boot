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

    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> of(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> of(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(0, "success", data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(0, "success", null);
    }

}
