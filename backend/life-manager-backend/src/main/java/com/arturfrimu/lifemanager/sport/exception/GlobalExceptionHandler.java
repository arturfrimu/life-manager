package com.arturfrimu.lifemanager.sport.exception;

import com.arturfrimu.lifemanager.error.domain.ErrorEvent;
import com.arturfrimu.lifemanager.error.service.ErrorEventStorage;
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
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
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument exception: {}", ex.getMessage());

        saveErrorToMinio("IllegalArgumentException", ex, null);

        var errorResponse = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                "/api/v1/exercises",
                null
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation exception: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        saveErrorToMinio("MethodArgumentNotValidException", ex, errors);

        var errorResponse = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Input validation failed",
                "/api/v1/exercises",
                errors
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: ", ex);

        saveErrorToMinio("Exception", ex, null);

        var errorResponse = new ErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred",
                "/api/v1/exercises",
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private void saveErrorToMinio(String eventType, Exception ex, Map<String, String> validationErrors) {
        try {
            var stackTrace = getStackTraceAsString(ex);
            
            Map<String, Object> details = new HashMap<>();
            details.put("stackTrace", stackTrace);
            details.put("exceptionClass", ex.getClass().getName());
            
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

    private String getStackTraceAsString(Exception ex) {
        var sw = new StringWriter();
        var pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
