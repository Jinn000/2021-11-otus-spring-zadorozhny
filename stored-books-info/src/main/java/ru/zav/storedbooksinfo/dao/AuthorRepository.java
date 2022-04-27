package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository {
    Author getById(String id);
    int deleteById(String id);
    Author save(Author author);
    List<Author> readAll() ;
    Optional<Author> findByFullName(FullName fullName);
}
