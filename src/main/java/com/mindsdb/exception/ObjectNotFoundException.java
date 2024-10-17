package com.mindsdb.exception;

/**
 * Exception thrown when an object is not found.
 * This class extends {@link Exception} to provide a specific
 * type of error indicating that the requested object is not found.
 */
public class ObjectNotFoundException extends Exception {

    public ObjectNotFoundException(String message) {
        super(message);
    }

}
