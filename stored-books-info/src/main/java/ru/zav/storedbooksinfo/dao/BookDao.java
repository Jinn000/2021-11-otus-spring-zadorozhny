package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.domain.AuthorSet;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> getById(String id) throws AppDaoException;
    int deleteById(String id) throws AppDaoException;
    int insert(Book book) throws AppDaoException;
    List<Book> readAll() throws AppDaoException;
    void clearAll() throws AppDaoException;
    Optional<String> findAuthorSetIdByBookId(String id) throws AppDaoException;
    List<Book> findByGenre(Genre genre) throws AppDaoException;
    List<Book> findByTitle(String title) throws AppDaoException;
}
