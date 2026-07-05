package com.shahrabi.interview.service.main.exception;

public class BookAlreadyDeletedException extends RuntimeException {
    public BookAlreadyDeletedException() {
        super();
    }

    public BookAlreadyDeletedException(String message) {
        super(message);
    }

    public BookAlreadyDeletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookAlreadyDeletedException(Throwable cause) {
        super(cause);
    }

    protected BookAlreadyDeletedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
