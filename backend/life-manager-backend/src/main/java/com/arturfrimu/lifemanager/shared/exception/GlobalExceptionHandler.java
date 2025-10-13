package com.arturfrimu.lifemanager.shared.exception;

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
    String APP_NAME;

    MinioErrorEventStorage minioErrorEventStorage;

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex, WebRequest request) {
        return handleException(
                ex,
                request,
                HttpStatus.BAD_REQUEST,
                ExceptionDetailsIdentifierUtils.originalExceptionName(ex),
                null
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return handleException(
                ex,
                request,
                HttpStatus.BAD_REQUEST,
                ExceptionDetailsIdentifierUtils.originalExceptionName(ex),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return handleException(
                ex,
                request,
                HttpStatus.BAD_REQUEST,
                ExceptionDetailsIdentifierUtils.originalExceptionName(ex),
                errors,
                "Validation Failed",
                "Input validation failed"
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        return handleException(
                ex,
                request,
                HttpStatus.BAD_REQUEST,
                ExceptionDetailsIdentifierUtils.originalExceptionName(ex),
                Collections.emptyMap(),
                "Type Mismatch",
                "Invalid parameter type"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        return handleException(
                ex,
                request,
                HttpStatus.INTERNAL_SERVER_ERROR,
                ExceptionDetailsIdentifierUtils.originalExceptionName(ex),
                null,
                "Internal Server Error",
                "An unexpected error occurred"
        );
    }

    private ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            WebRequest request,
            HttpStatus status,
            String eventType,
            Map<String, String> validationErrors
    ) {
        return handleException(ex, request, status, eventType, validationErrors, status.name(), ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            WebRequest request,
            HttpStatus status,
            String eventType,
            Map<String, String> validationErrors,
            String errorTitle,
            String errorMessage
    ) {
        var path = getRequestPath(request);
        var method = getRequestMethod(request);

        log.error("{} on {} {}: {}", eventType, method, path, ex.getMessage());

        saveErrorToMinio(eventType, ex, validationErrors, path, method);

        var errorResponse = new ErrorResponse(
                Instant.now(),
                status.value(),
                errorTitle,
                errorMessage,
                path,
                validationErrors
        );

        return ResponseEntity.status(status).body(errorResponse);
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

            minioErrorEventStorage.saveEventWithRetry(errorEvent);
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
