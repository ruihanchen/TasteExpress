package org.chendev.tasteexpress.server.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.error.ErrorCode;
import org.chendev.tasteexpress.common.exception.AuthException;
import org.chendev.tasteexpress.common.exception.BaseException;
import org.chendev.tasteexpress.common.exception.BusinessException;
import org.chendev.tasteexpress.common.result.Result;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * Centralized exception handling for all REST controllers.
 *
 * This class is responsible for:
 * - mapping domain exceptions to ErrorCode + HTTP status
 * - normalizing API responses to Result<T>
 * - ensuring internal details are never leaked to clients
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ------------------------
    // Domain / auth exceptions
    // ------------------------

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Result<Void>> handleAuth(AuthException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusiness(BusinessException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Result<Void>> handleBase(BaseException ex) {
        // Safety net for any future BaseException subclasses
        return buildResponse(ex);
    }

    // ------------------------
    // Validation / binding
    // ------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String msg = Optional.ofNullable(ex.getBindingResult().getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse(ErrorCode.VALIDATION_ERROR.getDefaultMessage());

        return buildResponse(ErrorCode.VALIDATION_ERROR, msg, ex);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Void>> handleBindException(BindException ex) {
        String msg = Optional.ofNullable(ex.getBindingResult().getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse(ErrorCode.VALIDATION_ERROR.getDefaultMessage());

        return buildResponse(ErrorCode.VALIDATION_ERROR, msg, ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                .findFirst()
                .map(v -> v.getMessage())
                .orElse(ErrorCode.VALIDATION_ERROR.getDefaultMessage());

        return buildResponse(ErrorCode.VALIDATION_ERROR, msg, ex);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
        String msg = "Missing required parameter: " + ex.getParameterName();
        return buildResponse(ErrorCode.BAD_REQUEST, msg, ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<Void>> handleNotReadable(HttpMessageNotReadableException ex) {
        return buildResponse(ErrorCode.PAYLOAD_MALFORMED, ErrorCode.PAYLOAD_MALFORMED.getDefaultMessage(), ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Result<Void>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return buildResponse(ErrorCode.METHOD_NOT_ALLOWED, ErrorCode.METHOD_NOT_ALLOWED.getDefaultMessage(), ex);
    }

    // ------------------------
    // Fallback
    // ------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(Result.error(errorCode.getCode(), errorCode.getDefaultMessage()));
    }

    // ------------------------
    // Helper methods
    // ------------------------

    private ResponseEntity<Result<Void>> buildResponse(BaseException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        String message = ex.getMessage();

        logAtAppropriateLevel(errorCode.getHttpStatus(), errorCode.getCode(), message, ex);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(Result.error(errorCode.getCode(), message));
    }

    private ResponseEntity<Result<Void>> buildResponse(ErrorCode errorCode, String message, Exception ex) {
        logAtAppropriateLevel(errorCode.getHttpStatus(), errorCode.getCode(), message, ex);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(Result.error(errorCode.getCode(), message));
    }

    private void logAtAppropriateLevel(HttpStatus status, String code, String message, Exception ex) {
        if (status.is5xxServerError()) {
            // Server-side issues should retain stack traces for debugging.
            log.error("Error code={}, message={}", code, message, ex);
        } else {
            // Client / validation / business errors are noisy if logged as error.
            log.warn("Error code={}, message={}", code, message);
        }
    }
}
