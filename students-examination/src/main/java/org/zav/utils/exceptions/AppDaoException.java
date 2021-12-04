package org.zav.utils.exceptions;

import java.io.IOException;

public class AppDaoException extends IOException {

    public AppDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
