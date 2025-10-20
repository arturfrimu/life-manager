package com.arturfrimu.lifemanager.shared.aspect;

import com.arturfrimu.lifemanager.shared.exception.ErrorEvent;
import com.arturfrimu.lifemanager.shared.util.ExceptionDetailsIdentifierUtils;
import com.arturfrimu.lifemanager.shared.util.storage.MinioErrorEventStorage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AsyncErrorAspect {

    MinioErrorEventStorage minioErrorEventStorage;

    @AfterThrowing(pointcut = "@annotation(org.springframework.scheduling.annotation.Scheduled)", throwing = "ex")
    public void handleScheduledError(Exception ex) throws Exception {
        var exceptionName = ExceptionDetailsIdentifierUtils.originalExceptionName(ex);
        var errorEvent = ErrorEvent.create(
                "ScheduledTaskException",
                "life-manager",
                ex.getMessage(),
                Map.of("task", "unknown", "stackTrace", ex.getStackTrace())
        );
        minioErrorEventStorage.saveEventWithRetry(errorEvent);
    }
}
