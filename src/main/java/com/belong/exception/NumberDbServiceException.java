package com.belong.exception;

public class NumberDbServiceException extends RuntimeException {
    public NumberDbServiceException(Throwable throwable) {
        super(throwable);
    }

    public NumberDbServiceException(String message) {
        super(message);
    }
}
