package ru.zav.storedbooksinfo.service;

import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Author add(FullName fullName);
    int delete(FullName fullName);
    Author rename(FullName oldName, FullName newName);
    List<Author> getAll();
    Optional<Author> findByFullName(FullName fullName);
    Optional<Author> findById(String id);
}
