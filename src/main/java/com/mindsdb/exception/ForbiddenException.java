package com.mindsdb.exception;

/**
 * Exception thrown when a forbidden action is attempted.
 * This class extends {@link Exception} to provide a specific
 * type of error indicating that the requested operation is not allowed.
 */
public class ForbiddenException extends Exception {

    /**
     * Constructs a new ForbiddenException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval
     *                by the {@link Throwable#getMessage()} method
     */
    public ForbiddenException(String message) {
        super(message);
    }
}
