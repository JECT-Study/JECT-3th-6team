package com.example.demo.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException exception, 
            HttpServletRequest request) {
        
        log.warn("Business Exception occurred: {}, path: {}", 
                exception.getMessage(), request.getRequestURI(), exception);
        
        ErrorResponse errorResponse = ErrorResponse.of(exception, request.getRequestURI());
        
        return ResponseEntity
                .status(exception.getErrorType().getHttpStatus())
                .body(errorResponse);
    }
    
    @ExceptionHandler(ParameterValidationException.class)
    public ResponseEntity<ErrorResponse> handleParameterValidationException(
            ParameterValidationException exception,
            HttpServletRequest request) {
        
        log.warn("Parameter Validation Exception occurred: {}, path: {}", 
                exception.getMessage(), request.getRequestURI(), exception);
        
        ErrorResponse errorResponse = ErrorResponse.of(exception, request.getRequestURI());
        
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        
        log.warn("Method Argument Not Valid Exception occurred: {}, path: {}", 
                exception.getMessage(), request.getRequestURI(), exception);
        
        List<ValidationError> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::convertFieldErrorToValidationError)
                .toList();
        
        ParameterValidationException parameterValidationException = 
                new ParameterValidationException(validationErrors);
        ErrorResponse errorResponse = 
                ErrorResponse.of(parameterValidationException, request.getRequestURI());
        
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
    
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(
            BindException exception,
            HttpServletRequest request) {
        
        log.warn("Bind Exception occurred: {}, path: {}", 
                exception.getMessage(), request.getRequestURI(), exception);
        
        List<ValidationError> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::convertFieldErrorToValidationError)
                .toList();
        
        ParameterValidationException parameterValidationException = 
                new ParameterValidationException(validationErrors);
        ErrorResponse errorResponse = 
                ErrorResponse.of(parameterValidationException, request.getRequestURI());
        
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException exception,
            HttpServletRequest request) {
        
        log.warn("Constraint Violation Exception occurred: {}, path: {}", 
                exception.getMessage(), request.getRequestURI(), exception);
        
        List<ValidationError> validationErrors = exception.getConstraintViolations()
                .stream()
                .map(this::convertConstraintViolationToValidationError)
                .toList();
        
        ParameterValidationException parameterValidationException = 
                new ParameterValidationException(validationErrors);
        ErrorResponse errorResponse = 
                ErrorResponse.of(parameterValidationException, request.getRequestURI());
        
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {
        
        log.warn("Method Argument Type Mismatch Exception occurred: {}, path: {}", 
                exception.getMessage(), request.getRequestURI(), exception);
        
        ValidationError validationError = ParameterValidationError.of(
                exception.getName(),
                exception.getValue(),
                "타입이 올바르지 않습니다. 예상 타입: " + exception.getRequiredType().getSimpleName()
        );
        
        ParameterValidationException parameterValidationException = 
                new ParameterValidationException(validationError);
        ErrorResponse errorResponse = 
                ErrorResponse.of(parameterValidationException, request.getRequestURI());
        
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception exception, 
            HttpServletRequest request) {
        
        log.error("Unexpected exception occurred: {}, path: {}", 
                exception.getMessage(), request.getRequestURI(), exception);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("내부 서버 오류가 발생했습니다.")
                .timestamp(java.time.LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity
                .status(500)
                .body(errorResponse);
    }
    
    private ValidationError convertFieldErrorToValidationError(FieldError fieldError) {
        return ParameterValidationError.of(
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage()
        );
    }
    
    private ValidationError convertConstraintViolationToValidationError(ConstraintViolation<?> violation) {
        String parameterName = violation.getPropertyPath().toString();
        return ParameterValidationError.of(
                parameterName,
                violation.getInvalidValue(),
                violation.getMessage()
        );
    }
}