package com.mindsdb.exception;

/**
 * Exception thrown when an unauthorized access attempt is made.
 * This class extends {@link Exception} to indicate that a request
 * requires user authentication that has either failed or has not
 * yet been provided.
 */
public class UnauthorizedException extends Exception {

    public UnauthorizedException(String message) {
        super(message);
    }

}
