package ru.zav.storedbooksinfo.service;

import ru.zav.storedbooksinfo.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Genre add(String genreDescription);
    int delete(String genreDescription);
    Genre rename(String oldDescription, String newDescription);
    List<Genre> getAll();
    Optional<Genre> findByDescription(String description);
}
