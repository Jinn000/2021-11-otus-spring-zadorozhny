package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;

public interface GenreDao {
    Genre getById(String id) throws AppDaoException;
    int deleteById(String id) throws AppDaoException;
    int insert(Genre genre) throws AppDaoException;
    List<Genre> readAll() throws AppDaoException;
    void clearAll() throws AppDaoException;
}
