package com.ivmarchenko.useraggregator.exception;

/**
 * Custom exception for errors occurring while fetching data from databases.
 */
public class AggregatorDatabaseFetchException extends RuntimeException {
    public AggregatorDatabaseFetchException(String message) {
        super(message);
    }

    public AggregatorDatabaseFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}