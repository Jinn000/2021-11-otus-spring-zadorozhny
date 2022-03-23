package ru.zav.storedbooksinfo.service;

import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Genre add(String genreDescription) throws AppServiceException;
    int delete(String genreDescription) throws AppServiceException;
    Genre rename(String oldDescription, String newDescription) throws AppServiceException;
    List<Genre> getAll() throws AppServiceException;
    Optional<Genre> findByDescription(String description) throws AppServiceException;
}
