package org.pencil.common.exception;

/**
 * 服务器异常，用于表示服务端内部错误
 * 
 * @author pencil
 * @date 24/10/01 18:32
 */
public class ServerException extends BaseException {

    private ServerException(Integer code, String message) {
        super(code, message);
    }

    /**
     * 创建一个带有自定义错误码和消息的服务器异常
     * 
     * @param code 错误码
     * @param message 错误消息
     * @return 服务器异常实例
     */
    public static ServerException of(Integer code, String message) {
        return new ServerException(code, message);
    }

    /**
     * 创建一个带有默认错误码和自定义消息的服务器异常
     * 
     * @param message 错误消息
     * @return 服务器异常实例
     */
    public static ServerException of(String message) {
        return new ServerException(-500, message);
    }

    /**
     * 从错误码枚举创建服务器异常
     * 
     * @param code 错误码枚举
     * @return 服务器异常实例
     */
    public static ServerException of(ErrorCode code) {
        return new ServerException(code.getCode(), code.getMessage());
    }
}
