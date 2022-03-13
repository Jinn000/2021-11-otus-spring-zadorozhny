package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    Author getById(String id) throws AppDaoException;
    int deleteById(String id) throws AppDaoException;
    int insert(Author author) throws AppDaoException;
    int update(Author author) throws AppDaoException;
    List<Author> readAll() throws AppDaoException;
    void clearAll() throws AppDaoException;
    Optional<Author> findByFullName(FullName fullName)throws AppDaoException;
}
