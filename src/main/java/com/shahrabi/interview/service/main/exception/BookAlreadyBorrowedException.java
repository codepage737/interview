package com.shahrabi.interview.service.main.exception;

public class BookAlreadyBorrowedException extends RuntimeException {
    public BookAlreadyBorrowedException() {
        super();
    }

    public BookAlreadyBorrowedException(String message) {
        super(message);
    }

    public BookAlreadyBorrowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookAlreadyBorrowedException(Throwable cause) {
        super(cause);
    }

    protected BookAlreadyBorrowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
