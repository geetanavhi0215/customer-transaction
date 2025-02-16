package com.customer.transaction.errors;

public class CustomerTransactionException extends RuntimeException {

    public CustomerTransactionException(String message) {
        super(message);
    }
}
