package com.saijyothi.bookstore.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String title) {
        super("Requested quantity exceeds available stock for: " + title);
    }
}
