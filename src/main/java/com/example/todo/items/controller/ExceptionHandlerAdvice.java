package com.example.todo.items.controller;

import com.example.todo.items.PastDueItemModificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(PastDueItemModificationException.class)
    public ResponseEntity<String> handleException() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Item is past due and can not be modified");
    }
}
