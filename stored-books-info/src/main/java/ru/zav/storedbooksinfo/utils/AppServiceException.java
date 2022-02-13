package ru.zav.storedbooksinfo.utils;

public class AppServiceException extends Exception {

    public AppServiceException(String message) {
        super(message);
    }

    public AppServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}