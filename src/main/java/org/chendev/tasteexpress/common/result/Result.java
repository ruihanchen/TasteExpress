package org.chendev.tasteexpress.common.result;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.chendev.tasteexpress.common.error.ApiError;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    /**
     * Indicates whether the request was handled successfully.
     */
    private boolean success;

    /**
     * Payload returned to the client when the request succeeds.
     */
    private T data;

    /**
     * Error information when success == false.
     * For simple cases, only "message" is required.
     */
    private ApiError error;

    // ---------- Factory methods ----------

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public ApiError getError() {
        return error;
    }

    private Result() {
        // use factory methods
    }

    /**
     * Factory method for successful responses with a payload.
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.success = true;
        result.data = data;
        return result;
    }

    /**
     * Factory method for a successful response without payload.
     * Useful for "command" style endpoints (create/update/delete).
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.success = true;
        return result;
    }

    /**
     * Factory method for error responses with a structured error code.
     * This is what we use from GlobalExceptionHandler.
     */
    public static <T> Result<T> error(String code, String message) {
        Result<T> result = new Result<>();
        result.success = false;
        result.error = new ApiError(code, message);
        return result;
    }
}
