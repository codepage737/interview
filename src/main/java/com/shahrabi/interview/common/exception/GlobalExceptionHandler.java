package com.shahrabi.interview.common.exception;

import com.shahrabi.interview.service.main.exception.BookAlreadyBorrowedException;
import com.shahrabi.interview.service.main.exception.BookAlreadyDeletedException;
import com.shahrabi.interview.service.main.exception.BookNotBorrowedException;
import com.shahrabi.interview.service.main.exception.DuplicateIsbnException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), null, locale);
        ErrorResponse error = new ErrorResponse(false, message, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex, Locale locale) {
        ErrorResponse error = new ErrorResponse(false, ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, Locale locale) {
        String message;
        if (ex instanceof AuthorizationDeniedException) {
            message = messageSource.getMessage("error.access.denied", null, locale);
        } else {
            message = messageSource.getMessage("error.server_error", null, locale);
        }
        ErrorResponse error = new ErrorResponse(false, message, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateIsbn(DuplicateIsbnException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), null, locale);
        return new ResponseEntity<>(new ErrorResponse(false, message, System.currentTimeMillis()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, Locale locale) {
        String message = messageSource.getMessage("error.database.error", null, locale);
        ErrorResponse error = new ErrorResponse(false, message, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex, Locale locale) {
        String message = messageSource.getMessage("error.concurrency.conflict", null, locale);
        ErrorResponse error = new ErrorResponse(false, message, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookAlreadyDeletedException.class)
    public ResponseEntity<ErrorResponse> handleBookAlreadyDeletedException(BookAlreadyDeletedException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), null, locale);
        ErrorResponse error = new ErrorResponse(false, message, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookAlreadyBorrowedException.class)
    public ResponseEntity<ErrorResponse> handleBookAlreadyBorrowedException(BookAlreadyBorrowedException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), null, locale);
        ErrorResponse error = new ErrorResponse(false, message, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookNotBorrowedException.class)
    public ResponseEntity<ErrorResponse> handleBookNotBorrowedException(BookNotBorrowedException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), null, locale);
        ErrorResponse error = new ErrorResponse(false, message, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
