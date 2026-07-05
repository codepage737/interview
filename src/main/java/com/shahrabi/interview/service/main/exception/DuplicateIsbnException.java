package com.shahrabi.interview.service.main.exception;

public class DuplicateIsbnException extends RuntimeException {
    public DuplicateIsbnException() {
        super();
    }

    public DuplicateIsbnException(String message) {
        super(message);
    }

    public DuplicateIsbnException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateIsbnException(Throwable cause) {
        super(cause);
    }

    protected DuplicateIsbnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
