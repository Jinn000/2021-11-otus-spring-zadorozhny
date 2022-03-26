package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.AuthorSet;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;
import java.util.UUID;

public interface AuthorSetRepository {
    AuthorSet getById(String id);
    List<AuthorSet> findByAuthorId(String authorId);
    List<AuthorSet> findByAuthorsSetId(String authorsSetId);
    int deleteById(String id);
    AuthorSet save(AuthorSet authorSet);
    List<AuthorSet> readAll();
    void clearAll();
}
