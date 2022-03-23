package ru.zav.storedbooksinfo.utils;

public class AppDaoException extends RuntimeException{

    public AppDaoException(String message) {
        super(message);
    }

    public AppDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
