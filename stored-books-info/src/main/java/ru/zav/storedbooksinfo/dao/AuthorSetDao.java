package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.AuthorSet;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;

public interface AuthorSetDao {
    AuthorSet getById(String id) throws AppDaoException;
    List<AuthorSet> findByAuthorId(String authorId) throws AppDaoException;
    List<AuthorSet> findByAuthorsSetId(String authorsSetId) throws AppDaoException;
    int deleteById(String id) throws AppDaoException;
    int insert(AuthorSet authorSet) throws AppDaoException;
    List<AuthorSet> readAll() throws AppDaoException;
    void clearAll() throws AppDaoException;
}
