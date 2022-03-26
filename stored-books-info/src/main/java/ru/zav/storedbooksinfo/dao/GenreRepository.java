package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.dao.datatypes.EntityId;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Genre getById(EntityId id);
    int deleteById(EntityId id);
    Genre save(Genre genre);
    List<Genre> readAll();
    void clearAll();
    Optional<Genre> findByDescription(String description);
}
