package ru.zav.storedbooksinfo.utils;

public class AppServiceException extends RuntimeException {

    public AppServiceException(String message) {
        super(message);
    }

    public AppServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}