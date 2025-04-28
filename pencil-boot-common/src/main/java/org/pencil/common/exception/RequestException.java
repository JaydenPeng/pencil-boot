package org.pencil.common.exception;

/**
 * 请求异常，用于表示客户端请求错误
 * 
 * @author pencil
 * @date 25/04/28 10:35
 */
public class RequestException extends BaseException {

    private RequestException(Integer code, String message) {
        super(code, message);
    }

    /**
     * 创建一个带有自定义错误码和消息的请求异常
     * 
     * @param code 错误码
     * @param message 错误消息
     * @return 请求异常实例
     */
    public static RequestException of(Integer code, String message) {
        return new RequestException(code, message);
    }

    /**
     * 创建一个带有默认错误码和自定义消息的请求异常
     * 
     * @param message 错误消息
     * @return 请求异常实例
     */
    public static RequestException of(String message) {
        return new RequestException(-500, message);
    }

    /**
     * 从错误码枚举创建请求异常
     * 
     * @param code 错误码枚举
     * @return 请求异常实例
     */
    public static RequestException of(ErrorCode code) {
        return new RequestException(code.getCode(), code.getMessage());
    }
}
