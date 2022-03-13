package ru.zav.storedbooksinfo.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.zav.storedbooksinfo.dao.BookDao;
import ru.zav.storedbooksinfo.dao.GenreDao;
import ru.zav.storedbooksinfo.dao.datatypes.EntityId;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.AppServiceException;
import ru.zav.storedbooksinfo.utils.UuidGeneratorNoDashes;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;
    private final BookDao bookDao;

    @Override
    public Genre add(String genreDescription) throws AppServiceException{
        if(StringUtils.isBlank(genreDescription)) throw new AppServiceException("Ошибка! Не указан Description для добавляемого жанра.");

        final String genreDescriptionTrimmed = StringUtils.trim(genreDescription);

        final Optional<Genre> genreOptional;
        try {
            genreOptional = genreDao.readAll().stream()
                    .filter(item -> StringUtils.upperCase(item.getDescription()).equals(StringUtils.upperCase(genreDescriptionTrimmed)))
                    .findFirst();
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        final Genre newGenre;
        if(genreOptional.isPresent()) return genreOptional.get();
        else{
            newGenre = new Genre(new UuidGeneratorNoDashes().generateUuid(), genreDescriptionTrimmed);
            try {
                genreDao.insert(newGenre);
            } catch (AppDaoException e) {
                throw new AppServiceException(e.getMessage(), e);
            }
        }

        return newGenre;
    }

    @Override
    public int delete(String genreDescription) throws AppServiceException {
        if(StringUtils.isBlank(genreDescription)) throw new AppServiceException("Ошибка! Не указан Description для удаляемого жанра.");

        final String genreDescriptionTrimmed = StringUtils.trim(genreDescription);

        final Optional<Genre> genreOptional;
        try {
            genreOptional = genreDao.findByDescription(genreDescriptionTrimmed);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        Integer deletedCount = genreOptional.filter(genre -> {
            try {
                return !isUsed(genre);
            } catch (AppServiceException e) {
                e.printStackTrace();
            }
            return false;
        })
                .map(Genre::getId)
                .map(EntityId::new)
                .map(id -> {
                    try {
                        return genreDao.deleteById(id);
                    } catch (AppDaoException e) {
                        e.printStackTrace();
                    }
                    return 0;
                })
                .orElse(0);

        return deletedCount;
    }

    @Override
    public Genre rename(String oldDescription, String newDescription) throws AppServiceException {
        if(StringUtils.isBlank(oldDescription) || StringUtils.isBlank(newDescription)) throw new AppServiceException("Ошибка! Не указан Description для переименования жанра.");

        final String oldDescriptionTrimmed = StringUtils.trim(oldDescription);
        final String newDescriptionTrimmed = StringUtils.trim(newDescription);

        final Optional<Genre> genreOptional;
        try {
            genreOptional = genreDao.findByDescription(oldDescriptionTrimmed);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        var updatedCount = genreOptional.map(genre -> {
            final Genre newGenre = new Genre(genre.getId(), newDescriptionTrimmed);
            try {
                return genreDao.insert(newGenre);
            } catch (AppDaoException e) {
                e.printStackTrace();
            }
            return 0;
        })
        .orElseThrow(() -> new AppServiceException(String.format("Не удалось найти жанр по названию: %s", oldDescriptionTrimmed)));

        if(updatedCount.equals(0)) throw new AppServiceException(String.format("Не удалось переименовать жанр %s в %s", oldDescriptionTrimmed, newDescriptionTrimmed));

        try {
            return genreDao.getById(genreOptional.map(Genre::getId)
                    .map(EntityId::new)
                    .orElseThrow(()-> new AppServiceException(String.format("Не удалось найти жанр по названию: %s", oldDescriptionTrimmed))));
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Genre> getAll() throws AppServiceException {
        final List<Genre> genreList;
        try {
            genreList = genreDao.readAll();
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
        return genreList;
    }

    @Override
    public Optional<Genre> findByDescription(String description) throws AppServiceException {
        try {
            return genreDao.findByDescription(description);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    private boolean isUsed(Genre genre) throws AppServiceException {
        final List<Book> bookList;
        try {
            bookList = bookDao.findByGenre(genre);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        return !bookList.isEmpty();
    }
}
