package com.belong.exception;

public class NumberNotFoundException extends RuntimeException {
    public NumberNotFoundException(String customer) {
        super("Number not found for customer : " + customer);
    }
}
