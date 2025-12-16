package org.chendev.tasteexpress.common.error;

import org.springframework.http.HttpStatus;
/**
 * Central place to define all application error codes.
 *
 * Each ErrorCode carries:
 * - a stable machine-readable code (e.g. "AUTH.ACCOUNT_NOT_FOUND")
 * - an HTTP status that reflects the error semantics
 * - a default human-readable message
 *
 * This becomes the single source of truth for error semantics
 * across controllers, services and exception handling.
 */
public enum ErrorCode {

    // ---------- Authentication / Account ----------
    AUTH_ACCOUNT_NOT_FOUND(
            "AUTH.ACCOUNT_NOT_FOUND",
            HttpStatus.NOT_FOUND,
            "Account not found"
    ),
    AUTH_ACCOUNT_LOCKED(
            "AUTH.ACCOUNT_LOCKED",
            HttpStatus.FORBIDDEN,
            "Account is locked"
    ),
    AUTH_ACCOUNT_EXIST(
            "AUTH.ACCOUNT_EXIST",
            HttpStatus.CONFLICT,
            "Account already exists"
    ),
    AUTH_LOGIN_FAILED(
            "AUTH.LOGIN_FAILED",
            HttpStatus.UNAUTHORIZED,
            "Invalid username or password"
    ),
    AUTH_USER_NOT_LOGIN(
            "AUTH.USER_NOT_LOGIN",
            HttpStatus.UNAUTHORIZED,
            "User is not logged in"
    ),
    AUTH_PASSWORD_ERROR(
            "AUTH.PASSWORD_ERROR",
            HttpStatus.UNAUTHORIZED,
            "Incorrect password"
    ),
    AUTH_PASSWORD_EDIT_FAILED(
            "AUTH.PASSWORD_EDIT_FAILED",
            HttpStatus.BAD_REQUEST,
            "Failed to update password"
    ),

    // ---------- Address book ----------
    ADDR_BUSINESS_ERROR(
            "ADDR.BUSINESS_ERROR",
            HttpStatus.BAD_REQUEST,
            "Address book operation failed"
    ),

    // ---------- Orders ----------
    ORDER_BUSINESS_ERROR(
            "ORDER.BUSINESS_ERROR",
            HttpStatus.UNPROCESSABLE_ENTITY,
            "Order cannot be processed"
    ),

    // ---------- Shopping cart ----------
    CART_BUSINESS_ERROR(
            "CART.BUSINESS_ERROR",
            HttpStatus.UNPROCESSABLE_ENTITY,
            "Shopping cart operation failed"
    ),
    CATEGORY_NOT_FOUND(
            "CATEGORY.NOT_FOUND",
            HttpStatus.CONFLICT,
            "Category cannot be found"
    ),

    // ---------- Dish / Category / Setmeal ----------
    DISH_DELETION_NOT_ALLOWED(
            "DISH.DELETION_NOT_ALLOWED",
            HttpStatus.CONFLICT,
            "Dish cannot be deleted in current state"
    ),
    DISH_NOT_FOUND(
            "DISH.NOT_FOUND",
            HttpStatus.CONFLICT,
            "Dish cannot be found"
    ),
    SETMEAL_NOT_FOUND(
            "SETMEAL.NOT_FOUND",
            HttpStatus.CONFLICT,
            "Setmeal cannot be found"
    ),
    SETMEAL_ON_SALE(
            "SETMEAL.ON_SALE",
            HttpStatus.CONFLICT,
            "Meal is on sale"
    ),
    CATEGORY_DELETION_NOT_ALLOWED(
            "CATEGORY.DELETION_NOT_ALLOWED",
            HttpStatus.CONFLICT,
            "Category cannot be deleted in current state"
    ),
    SETMEAL_ENABLE_FAILED(
            "SETMEAL.ENABLE_FAILED",
            HttpStatus.UNPROCESSABLE_ENTITY,
            "Setmeal cannot be enabled"
    ),

    // ---------- Validation / Request-level ----------
    VALIDATION_ERROR(
            "COMMON.VALIDATION_ERROR",
            HttpStatus.BAD_REQUEST,
            "Validation error"
    ),
    BAD_REQUEST(
            "COMMON.BAD_REQUEST",
            HttpStatus.BAD_REQUEST,
            "Bad request"
    ),
    METHOD_NOT_ALLOWED(
            "COMMON.METHOD_NOT_ALLOWED",
            HttpStatus.METHOD_NOT_ALLOWED,
            "HTTP method not allowed"
    ),
    PAYLOAD_MALFORMED(
            "COMMON.PAYLOAD_MALFORMED",
            HttpStatus.BAD_REQUEST,
            "Malformed JSON payload"
    ),
    CONFLICT(
            "COMMON.CONFLICT",
            HttpStatus.CONFLICT,
            "Request conflicts with current resource state"
    ),

    // ---------- Generic / fallback ----------
    BUSINESS_ERROR(
            "COMMON.BUSINESS_ERROR",
            HttpStatus.UNPROCESSABLE_ENTITY,
            "Business rule violated"
    ),
    INTERNAL_ERROR(
            "COMMON.INTERNAL_ERROR",
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error"
    ),
    DUPLICATE_NAME(
            "DUPLICATE_NAME",
            HttpStatus.CONFLICT,
            "Name already exists"
    )

    ;


    private final String code;
    private final HttpStatus httpStatus;
    private final String defaultMessage;

    ErrorCode(String code, HttpStatus httpStatus, String defaultMessage) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
