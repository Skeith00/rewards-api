package uoc.rewards.rewardsapi.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uoc.rewards.rewardsapi.common.exception.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles exception raised by the DMS controller
 */
@RestControllerAdvice
public class RewardsControllerAdvice {

    private ResponseEntity<Map<String, Object>> handle(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(getErrorAttributes(status, message));
    }

    private Map<String, Object> getErrorAttributes(HttpStatus status, String message) {
        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("timestamp", new Date());
        attributes.put("error", status.getReasonPhrase());
        attributes.put("status", status.value());
        attributes.put("message", message);
        return attributes;
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Map<String, Object>> handleBadRequest(BadFormatException e) {
        return handle(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException e) {
        return handle(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException e) {
        return handle(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<Map<String, Object>> handleForbiddenException(ForbiddenException e) {
        return handle(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Map<String, Object>> handleException(Exception e) {
        return handle(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}
