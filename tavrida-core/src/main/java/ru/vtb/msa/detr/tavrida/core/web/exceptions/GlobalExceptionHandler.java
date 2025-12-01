package ru.vtb.msa.detr.tavrida.core.web.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.vtb.msa.detr.tavrida.core.exception.AuthenticationTavridaException;
import ru.vtb.msa.detr.tavrida.core.exception.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Глобальный обработчик исключений для REST-контроллеров
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Создаём логгер для этого класса
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(
            Exception ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred");
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // В продакшене логируйте исключение, но не показывайте стек в ответе!
        logger.error("Unexpected error", ex);

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationTavridaException.class)
    public ResponseEntity<Object> handleAuthenticationTavridaException(
            Exception ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Authorized Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // В продакшене логируйте исключение, но не показывайте стек в ответе!
        logger.error("Authorized Server Error", ex);

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(
            Exception ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // В продакшене логируйте исключение, но не показывайте стек в ответе!
        logger.error("Unexpected error", ex);

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }


    // Обработка ошибок валидации (если используете @Valid)
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) { ... }
}