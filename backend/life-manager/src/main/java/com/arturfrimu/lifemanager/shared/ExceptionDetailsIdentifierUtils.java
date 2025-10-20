package com.arturfrimu.lifemanager.shared;

import lombok.experimental.UtilityClass;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.sql.SQLException;

@UtilityClass
public final class ExceptionDetailsIdentifierUtils {

    public String originalExceptionName(Throwable throwable) {
        if (throwable == null) return "UnknownException";

        return switch (throwable) {
            case IllegalArgumentException _ -> IllegalArgumentException.class.getSimpleName();
            case NullPointerException _ -> NullPointerException.class.getSimpleName();
            case IllegalStateException _ -> IllegalStateException.class.getSimpleName();
            case MethodArgumentNotValidException _ -> MethodArgumentNotValidException.class.getSimpleName();
            case DataAccessException _ -> DataAccessException.class.getSimpleName();
            case SQLException _ -> SQLException.class.getSimpleName();
            case RestClientException _ -> RestClientException.class.getSimpleName();
            case IOException _ -> IOException.class.getSimpleName();
            case RuntimeException _ -> RuntimeException.class.getSimpleName();
            case Exception ex -> ex.getClass().getSimpleName();
            default -> throwable.getClass().getSimpleName();
        };
    }

}

