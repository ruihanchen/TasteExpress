package org.chendev.tasteexpress.common.exception;

import org.chendev.tasteexpress.common.error.ErrorCode;

/**
 * Exception type for authentication / authorization related failures.
 *
 * Typical use cases:
 * - account not found
 * - incorrect password
 * - account locked
 * - user not logged in
 */
public class AuthException extends BaseException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
