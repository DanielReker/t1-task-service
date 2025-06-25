package io.github.danielreker.t1taskservice.exception;

public class ConcurrentTaskModificationConflictException extends RuntimeException {
    public ConcurrentTaskModificationConflictException(String message) {
        super(message);
    }
}
