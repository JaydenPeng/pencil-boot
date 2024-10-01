package org.pencil.common.exception;

/**
 * @author pencil
 * @date 24/10/01 18:32
 */
public class PencilException extends RuntimeException {

    private PencilException(String message) {
        super(message);
    }

    public static PencilException of(String message) {
        return new PencilException(message);
    }

}
