package com.ivmarchenko.useraggregator.exception;

/**
 * Custom exception for errors occurring in UserService.
 */
public class AggregatorServiceException extends RuntimeException {
    public AggregatorServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}