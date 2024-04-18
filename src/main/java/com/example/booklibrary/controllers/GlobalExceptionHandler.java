package com.example.booklibrary.controllers;

import com.example.booklibrary.exceptions.ErrorResponse;
import com.example.booklibrary.exceptions.ListEmptyException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleInternalServerError(Exception ex) {
        log.error("Internal Server Error.", ex);
        return new ErrorResponse(LocalDateTime.now(), "Internal Server Error", ex.getClass().getSimpleName());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            HttpClientErrorException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class,
            BadRequestException.class
    })
    public ErrorResponse handleBadRequestException(Exception ex) {
        log.error("Bad Request", ex);
        return new ErrorResponse(LocalDateTime.now(), "Bad Request", ex.getClass().getSimpleName());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponse handleMethodNotAllowed(Exception ex) {
        log.error("Method Not Allowed", ex);
        return new ErrorResponse(LocalDateTime.now(), "Method Not Allowed.", ex.getClass().getSimpleName());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            NoHandlerFoundException.class,
            EntityNotFoundException.class
    })
    public ErrorResponse handleNotFoundException(Exception ex) {
        log.error("Not Found", ex);
        return new ErrorResponse(LocalDateTime.now(), "Not Found", ex.getClass().getSimpleName());
    }

    @ExceptionHandler(ListEmptyException.class)
    public ResponseEntity<String> handleBookListEmptyException(ListEmptyException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}