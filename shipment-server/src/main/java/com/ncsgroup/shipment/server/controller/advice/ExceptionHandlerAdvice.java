package com.ncsgroup.shipment.server.controller.advice;


import com.ncsgroup.shipment.server.dto.Error;
import com.ncsgroup.shipment.server.dto.ResponseGeneral;
import com.ncsgroup.shipment.server.exception.base.BaseException;
import com.ncsgroup.shipment.server.constanst.Constants;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {
  private final MessageSource messageSource;

  @ExceptionHandler(value = {BaseException.class})
  public ResponseEntity<ResponseGeneral<Error>> handleFinanceBaseException(
        BaseException ex,
        WebRequest webRequest
  ) {
    return ResponseEntity
          .status(ex.getStatus())
          .body(getError(ex.getStatus(), ex.getCode(), webRequest.getLocale(), ex.getParams()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseGeneral<Error>> handleValidationExceptions(
        MethodArgumentNotValidException exception,
        WebRequest webRequest
  ) {
    log.error("(handleValidationExceptions)exception: {}", exception.getMessage());
    String language = Objects.nonNull(webRequest.getHeader(Constants.CommonConstants.LANGUAGE)) ?
          webRequest.getHeader(Constants.CommonConstants.LANGUAGE) : Constants.CommonConstants.DEFAULT_LANGUAGE;

    String errorMessage = exception.getBindingResult().getFieldErrors().stream()
          .map(fieldError -> fieldError.getDefaultMessage())
          .findFirst()
          .orElse(exception.getMessage());

    log.error("(handleValidationExceptions) {}", errorMessage);
    return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(getError(HttpStatus.BAD_REQUEST.value(), errorMessage, language));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ResponseGeneral<Error>> handleConstraintViolationExceptions(
        ConstraintViolationException exception,
        WebRequest webRequest
  ) {
    log.error("(handleConstraintViolationExceptions) exception: {}", exception.getMessage());
    String language = Objects.nonNull(webRequest.getHeader(Constants.CommonConstants.LANGUAGE)) ?
          webRequest.getHeader(Constants.CommonConstants.LANGUAGE) : Constants.CommonConstants.DEFAULT_LANGUAGE;

    String errorMessage = exception.getConstraintViolations().stream()
          .map(constraintViolation -> constraintViolation.getMessage())
          .findFirst()
          .orElse(exception.getMessage());

    log.error("(handleConstraintViolationExceptions) {}", errorMessage);
    return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(getError(HttpStatus.BAD_REQUEST.value(), errorMessage, language));
  }

  private ResponseGeneral<Error> getError(int status, String code, String language) {
    return ResponseGeneral.of(
          status,
          HttpStatus.valueOf(status).getReasonPhrase(),
          Error.of(code, getMessage(code, new Locale(language)))
    );
  }

  private ResponseGeneral<Error> getError(int status, String code, Map<String, String> params) {
    return ResponseGeneral.of(
          status,
          HttpStatus.valueOf(status).getReasonPhrase(),
          Error.of(code, params)
    );
  }

  private ResponseGeneral<Error> getError(int status, String code, Locale locale, Map<String, String> params) {
    return ResponseGeneral.of(
          status,
          HttpStatus.valueOf(status).getReasonPhrase(),
          Error.of(code, getMessage(code, locale, params))
    );
  }

  private String getMessage(String code, Locale locale, Map<String, String> params) {
    var message = getMessage(code, locale);
    if (params != null && !params.isEmpty()) {
      for (var param : params.entrySet()) {
        message = message.replace(getMessageParamsKey(param.getKey()), param.getValue());
      }
    }
    return message;
  }

  private String getMessage(String code, Locale locale) {
    try {
      return messageSource.getMessage(code, null, locale);
    } catch (Exception ex) {
      return code;
    }
  }

  private String getMessageParamsKey(String key) {
    return "%" + key + "%";
  }
}
