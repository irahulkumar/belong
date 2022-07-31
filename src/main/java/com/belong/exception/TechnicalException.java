package com.belong.exception;

public class TechnicalException extends RuntimeException {

    public static final String TECH_ERROR = "Error in accessing DB";
    private final String details;

    public TechnicalException(String message, String details) {
        super(message);
        this.details = details;
    }

    @Override
    public String getMessage() {
        return details;
    }

}
