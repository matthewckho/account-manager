package com.acmebank.accountManager.exception;

import com.acmebank.accountManager.data.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.PersistenceException;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    protected ResponseEntity<Object> handleAccountNotFound(AccountNotFoundException ex, WebRequest request) {
        return responseError(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({TransferBetweenSameAccountException.class, InsufficientFundException.class, InvalidCurrencyException.class})
    protected ResponseEntity<Object> handleAccountManagerException(AccountManagerException ex, WebRequest request) {
        return responseError(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(PersistenceException.class)
    protected ResponseEntity<Object> handleLockException(PersistenceException ex, WebRequest request) {
        return responseError(ex, HttpStatus.FORBIDDEN, request);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return responseError(ex, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<Object> responseError(Exception ex, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, ErrorResponse.builder().message(ex.getMessage()).status(status.toString()).build(), new HttpHeaders(), status, request);
    }
}
