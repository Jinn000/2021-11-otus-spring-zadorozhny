package ru.zav.storedbooksinfo.utils;

public class AppDomainException extends Exception {

    public AppDomainException(String message) {
        super(message);
    }

    public AppDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
