package ru.zav.storedbooksinfo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.dao.BookRepository;
import ru.zav.storedbooksinfo.dao.GenreRepository;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Override
    public Genre add(String genreDescription) {
        if(StringUtils.isBlank(genreDescription)) throw new AppServiceException("Ошибка! Не указан Description для добавляемого жанра.");

        try {
            final Optional<Genre> genreOptional = genreRepository.findByDescription(genreDescription);
            return genreOptional.orElseGet(() -> genreRepository.save(new Genre(null, StringUtils.trim(genreDescription))));
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Override
    public int delete(String genreDescription) {
        if(StringUtils.isBlank(genreDescription)) throw new AppServiceException("Ошибка! Не указан Description для удаляемого жанра.");

        final String genreDescriptionTrimmed = StringUtils.trim(genreDescription);

        final Optional<Genre> genreOptional;
        try {
            genreOptional = genreRepository.findByDescription(genreDescriptionTrimmed);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        return genreOptional.filter(genre -> {
            try {
                return !isUsed(genre);
            } catch (AppServiceException e) {
                log.error(e.getLocalizedMessage());
            }
            return false;
        })
                .map(Genre::getId)
                .map(id -> {
                    try {
                        genreRepository.deleteById(id);
                        return 1;
                    } catch (EmptyResultDataAccessException e) {
                        log.error(e.getLocalizedMessage());
                    }
                    return 0;
                })
                .orElse(0);
    }

    @Override
    public Genre rename(String oldDescription, String newDescription) {
        if(StringUtils.isBlank(oldDescription) || StringUtils.isBlank(newDescription)) throw new AppServiceException("Ошибка! Не указан Description для переименования жанра.");

        final String oldDescriptionTrimmed = StringUtils.trim(oldDescription);
        final String newDescriptionTrimmed = StringUtils.trim(newDescription);

        final Optional<Genre> genreOptional;
        try {
            genreOptional = genreRepository.findByDescription(oldDescriptionTrimmed);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        Genre updatedGenre = genreOptional.map(genre -> {
            try {
                return genreRepository.save(new Genre(genre.getId(), newDescriptionTrimmed));
            } catch (AppDaoException e) {
                log.error(e.getLocalizedMessage());
            }
            return genre;
        })
        .orElseThrow(() -> new AppServiceException(String.format("Не удалось найти жанр по названию: %s", oldDescriptionTrimmed)));

        if(!updatedGenre.getDescription().equals(newDescriptionTrimmed)) throw new AppServiceException(String.format("Не удалось переименовать жанр %s в %s", oldDescriptionTrimmed, newDescriptionTrimmed));

        return updatedGenre;
    }

    @Override
    public List<Genre> getAll() {
        try {
            return genreRepository.findAll();
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Genre> findByDescription(String description) {
        try {
            return genreRepository.findByDescription(description);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    private boolean isUsed(Genre genre) {
        try {
            return !bookRepository.findByGenre(genre).isEmpty();
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }
}
