package com.saijyothi.bookstore.exception;

public class BookOutOfStockException extends RuntimeException {
    public BookOutOfStockException(String title) {
        super("Book is out of stock: " + title);
    }
}
