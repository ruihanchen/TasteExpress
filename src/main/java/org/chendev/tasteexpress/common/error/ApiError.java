package org.chendev.tasteexpress.common.error;

/**
 * Standard error payload exposed to API consumers.
 * This allows clients to rely on a stable error.code
 * while still showing a human-readable message.
 */
public class ApiError {

    private final String code;
    private final String message;

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
