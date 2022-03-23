package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.dao.datatypes.EntityId;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Genre getById(EntityId id) throws AppDaoException;
    int deleteById(EntityId id) throws AppDaoException;
    int insert(Genre genre) throws AppDaoException;
    List<Genre> readAll() throws AppDaoException;
    void clearAll() throws AppDaoException;
    Optional<Genre> findByDescription(String description) throws AppDaoException;
}
