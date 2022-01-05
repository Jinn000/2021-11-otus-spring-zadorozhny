package org.zav.service;

import org.springframework.core.io.Resource;
import org.zav.utils.exceptions.AppDaoException;

public interface ResourceHolder {
    Resource getResource(String path);
}
