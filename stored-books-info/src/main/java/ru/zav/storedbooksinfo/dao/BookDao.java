package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;

public interface BookDao {
    Book getById(String id) throws AppDaoException;
    int deleteById(String id) throws AppDaoException;
    int insert(Book book) throws AppDaoException;
    List<Book> readAll() throws AppDaoException;
    void clearAll() throws AppDaoException;
}
