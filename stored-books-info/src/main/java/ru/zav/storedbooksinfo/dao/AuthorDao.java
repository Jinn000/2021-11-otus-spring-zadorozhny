package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;

public interface AuthorDao {
    Author getById(String id) throws AppDaoException;
    int deleteById(String id) throws AppDaoException;
    int insert(Author author) throws AppDaoException;
    List<Author> readAll() throws AppDaoException;
    void clearAll() throws AppDaoException;
}
