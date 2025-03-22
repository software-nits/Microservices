package com.software.orderservice.controlleradvice;

import com.software.orderservice.exception.OrderNotFoundException;
import com.software.orderservice.exception.TooManyRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderControllerAdvice {

    @ExceptionHandler(value = OrderNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String userNotFoundException(OrderNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = TooManyRequestException.class)
    @ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<String> userNotFoundException(TooManyRequestException exception) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(exception.getMessage());
    }
}
