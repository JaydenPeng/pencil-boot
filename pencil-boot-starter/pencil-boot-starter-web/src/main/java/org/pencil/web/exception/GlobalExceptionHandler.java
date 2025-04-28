package org.pencil.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.pencil.common.beans.resp.Result;
import org.pencil.common.exception.RequestException;
import org.pencil.common.exception.ServerException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pencil
 * @date 24/10/11 21:34
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("global catch exception", e);
        return new ResponseEntity<>(Result.of(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Result<Void>> handleServerException(ServerException e) {
        log.error("global catch server exception", e);
        return new ResponseEntity<>(Result.of(e.getCode(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<Result<Void>> handleRequestException(RequestException e) {
        log.error("global catch request exception", e);
        return new ResponseEntity<>(Result.of(e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleParamValidException(MethodArgumentNotValidException e) {
        log.error("global catch param valid exception", e);
        Map<String, String> errors = new HashMap<>(e.getErrorCount());
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(Result.of(-1, "param error", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().iterator().next().getMessage();
        return new ResponseEntity<>(Result.of(-1, "param valid error", message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(Result.of(-1, "param error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Result<Void>> handleDataAccessException(DataAccessException e) {
        log.error("global catch data access exception", e);
        return new ResponseEntity<>(Result.of("sql execute error", e.getClass().getName()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
