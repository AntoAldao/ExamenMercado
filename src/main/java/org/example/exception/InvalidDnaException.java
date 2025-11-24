package org.example.exception;

/**
 * Excepción custom para errores de validación de ADN.
 */
public class InvalidDnaException extends RuntimeException {
    
    public InvalidDnaException(String message) {
        super(message);
    }
    
    public InvalidDnaException(String message, Throwable cause) {
        super(message, cause);
    }
}
