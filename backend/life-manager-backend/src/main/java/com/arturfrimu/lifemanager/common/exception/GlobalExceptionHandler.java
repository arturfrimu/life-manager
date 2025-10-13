package com.arturfrimu.lifemanager.common.exception;

import com.arturfrimu.lifemanager.common.error.domain.ErrorEvent;
import com.arturfrimu.lifemanager.common.error.service.ErrorEventStorage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {

    @NonFinal
    @Value("${spring.application.name:life-manager}")
    private String APP_NAME;

    ErrorEventStorage errorEventStorage;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        var path = getRequestPath(request);
        var method = getRequestMethod(request);
        log.error("Illegal argument exception on {} {}: {}", method, path, ex.getMessage());
        var exceptionName = ExceptionDetailsIdentifierUtils.originalExceptionName(ex);
        saveErrorToMinio(exceptionName, ex, null, path, method);

        var errorResponse = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                path,
                null
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        var path = getRequestPath(request);
        var method = getRequestMethod(request);
        log.error("Validation exception on {} {}: {}", method, path, ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        saveErrorToMinio("MethodArgumentNotValidException", ex, errors, path, method);

        var errorResponse = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Input validation failed",
                path,
                errors
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        var path = getRequestPath(request);
        var method = getRequestMethod(request);
        log.error("Type mismatch exception on {} {}: {}", method, path, ex.getMessage());

        saveErrorToMinio("MethodArgumentTypeMismatchException", ex, Collections.emptyMap(), path, method);

        var errorResponse = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Input validation failed",
                path,
                Collections.emptyMap()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        var path = getRequestPath(request);
        var method = getRequestMethod(request);
        log.error("Unexpected error on {} {}: ", method, path, ex);

        saveErrorToMinio("Exception", ex, null, path, method);

        var errorResponse = new ErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred",
                path,
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private void saveErrorToMinio(String eventType, Exception ex, Map<String, String> validationErrors, String path, String method) {
        try {
            var stackTrace = getStackTraceAsString(ex);
            
            Map<String, Object> details = new HashMap<>();
            details.put("stackTrace", stackTrace);
            details.put("exceptionClass", ex.getClass().getName());
            details.put("requestPath", path);
            details.put("requestMethod", method);
            
            if (validationErrors != null && !validationErrors.isEmpty()) {
                details.put("validationErrors", validationErrors);
            }
            
            var errorEvent = ErrorEvent.create(
                    eventType,
                    APP_NAME,
                    ex.getMessage() != null ? ex.getMessage() : "No message available",
                    details
            );
            
            errorEventStorage.saveEventWithRetry(errorEvent);
        } catch (Exception e) {
            log.error("Critical: Failed to save error event", e);
        }
    }

    private String getRequestPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            var httpRequest = servletWebRequest.getRequest();
            var queryString = httpRequest.getQueryString();
            var requestURI = httpRequest.getRequestURI();
            return queryString != null ? "%s?%s".formatted(requestURI, queryString) : requestURI;
        }
        return request.getDescription(false).replace("uri=", "");
    }

    private String getRequestMethod(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getMethod();
        }
        return "UNKNOWN";
    }

    private String getStackTraceAsString(Exception ex) {
        var sw = new StringWriter();
        var pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
