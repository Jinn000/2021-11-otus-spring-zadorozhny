package ru.zav.storedbooksinfo.service;

import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;

public interface GenreService {
    Genre add(String genreDescription) throws AppServiceException;
    int delete(String genreDescription) throws AppServiceException;
    Genre rename(String oldDescription, String newDescription) throws AppServiceException;
    List<Genre> getAll() throws AppServiceException;
}
