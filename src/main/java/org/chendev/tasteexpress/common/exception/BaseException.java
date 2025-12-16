package org.chendev.tasteexpress.common.exception;

import org.chendev.tasteexpress.common.error.ErrorCode;

/**
 * Base type for all domain-specific exceptions in the system.
 *
 * Each exception is associated with an ErrorCode, which in turn
 * defines the HTTP status and default error message.
 */
public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * Use this constructor when you are fine with the default error message
     * defined on the corresponding ErrorCode.
     */
    protected BaseException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    /**
     * Use this constructor when you want to override the default message
     * with a more specific description (e.g., remaining attempts).
     */
    protected BaseException(ErrorCode errorCode, String message) {
        super(message != null ? message : errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}