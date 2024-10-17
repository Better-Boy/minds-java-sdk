package com.mindsdb.exception;

/**
 * Exception thrown to indicate that an unknown error has occurred.
 * This class extends {@link Exception} and is used when an error
 * cannot be specifically categorized or identified.
 */
public class UnknownError extends Exception {

    public UnknownError(String message) {
        super(message);
    }
}
