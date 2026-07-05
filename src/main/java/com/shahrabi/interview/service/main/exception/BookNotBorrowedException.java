package com.shahrabi.interview.service.main.exception;

public class BookNotBorrowedException extends RuntimeException {
    public BookNotBorrowedException() {
        super();
    }

    public BookNotBorrowedException(String message) {
        super(message);
    }

    public BookNotBorrowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookNotBorrowedException(Throwable cause) {
        super(cause);
    }

    protected BookNotBorrowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
