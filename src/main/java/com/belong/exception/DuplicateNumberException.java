package com.belong.exception;

public class DuplicateNumberException extends RuntimeException {
    public DuplicateNumberException(String number) {
        super("Number already exists : " + number);
    }
}
