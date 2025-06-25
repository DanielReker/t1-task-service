package io.github.danielreker.t1taskservice.controller;

import io.github.danielreker.t1taskservice.exception.ConcurrentTaskModificationConflictException;
import io.github.danielreker.t1taskservice.exception.InvalidTaskStateException;
import io.github.danielreker.t1taskservice.exception.TaskNotFoundException;
import io.github.danielreker.t1taskservice.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTaskNotFoundException(TaskNotFoundException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidTaskStateException.class)
    public ResponseEntity<ErrorDto> handleTaskNotFoundException(InvalidTaskStateException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ConcurrentTaskModificationConflictException.class)
    public ResponseEntity<ErrorDto> handleTaskNotFoundException(ConcurrentTaskModificationConflictException e) {
        return createErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }


    private ResponseEntity<ErrorDto> createErrorResponse(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorDto(status.value(), message));
    }
}
