package org.chendev.tasteexpress.common.exception;

import org.chendev.tasteexpress.common.error.ErrorCode;

/**
 * Generic business exception for domain-level rules.
 *
 * For example:
 * - deleting a category that is still linked to dishes
 * - enabling a setmeal while some dishes are disabled
 * - order state transitions that are not allowed
 */
public class BusinessException extends BaseException {

  public BusinessException(ErrorCode errorCode) {
    super(errorCode);
  }

  public BusinessException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
