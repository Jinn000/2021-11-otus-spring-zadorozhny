package ru.zav.storedbooksinfo.service;

import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;

public interface AuthorService {
    Author add(FullName fullName) throws AppServiceException;
    void delete(FullName fullName) throws AppServiceException;
    Author rename(FullName oldName, FullName newName) throws AppServiceException;
    List<Author> getAll() throws AppServiceException;
}
