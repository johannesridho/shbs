package com.shbs.common.exception;

public abstract class ShbsException extends RuntimeException {

    public ShbsException() {}

    public abstract String getErrorCode();

    public ShbsException(String message) {
        super(message);
    }

    public ShbsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShbsException(Throwable cause) {
        super(cause);
    }

    public ShbsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
