package ru.didenko.smartconsulting.seasonalservices.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.didenko.smartconsulting.seasonalservices.exceptions.ApplicationFailException;
import ru.didenko.smartconsulting.seasonalservices.exceptions.ConstraintsException;
import ru.didenko.smartconsulting.seasonalservices.exceptions.IncorrectIdException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(IncorrectIdException.class)
    public ResponseEntity<?> handleIdException(IncorrectIdException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ConstraintsException.class)
    public ResponseEntity<?> handleConstraintsException(ConstraintsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(ApplicationFailException.class)
    public ResponseEntity<?> handleApplicationException(ApplicationFailException e) {
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(e.getMessage());
    }
}
