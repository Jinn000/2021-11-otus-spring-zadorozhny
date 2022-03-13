package ru.zav.storedbooksinfo.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.zav.storedbooksinfo.dao.AuthorDao;
import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.AppServiceException;
import ru.zav.storedbooksinfo.utils.UuidGeneratorNoDashes;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;

    @Override
    public Author add(@NotNull FullName fullName) throws AppServiceException {

        final Author newAuthor;
        try {
            newAuthor = new Author(new UuidGeneratorNoDashes().generateUuid(), fullName.getFirstName(), fullName.getLastName(), fullName.getFamilyName());
            authorDao.insert(newAuthor);
        } catch (AppDaoException e) {
            throw new AppServiceException(String.format("Не удалось добавить автора %s. Причина: %s", fullName.toString(), e.getLocalizedMessage()), e);
        }
        return newAuthor;
    }

    @Override
    public void delete(@NotNull FullName fullName) throws AppServiceException {
        if(StringUtils.isBlank(fullName.getFirstName())) throw new AppServiceException("Ошибка! Не указано Имя автора.");
        if(StringUtils.isBlank(fullName.getLastName())) throw new AppServiceException("Ошибка! Не указано Отчество автора.");
        if(StringUtils.isBlank(fullName.getFamilyName())) throw new AppServiceException("Ошибка! Не указана Фамилия автора.");

        final Optional<Author> authorOptional;
        try {
            authorOptional = authorDao.findByFullName(fullName);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        var deletedCountOpt = authorOptional.map(Author::getId)
                .map(id -> {
                    try {
                        return authorDao.deleteById(id);
                    } catch (AppDaoException e) {
                        e.printStackTrace();
                    }
                    return 0;
                });
    }

    @Override
    public Author rename(FullName oldName, FullName newName) throws AppServiceException {
        final Optional<Author> authorOptional;
        try {
            authorOptional = authorDao.findByFullName(oldName);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        var updatedCount = authorOptional.map(author -> {
            final Author newAuthor = new Author(author.getId(), newName.getFirstName(), newName.getLastName(), newName.getFamilyName());
            try {
                return authorDao.update(newAuthor);
            } catch (AppDaoException e) {
                e.printStackTrace();
            }
            return 0;
        }).orElseThrow(() -> new AppServiceException(String.format("Не удалось найти автора: %s", oldName.toString())));

        if(updatedCount.equals(0)) throw new AppServiceException(String.format("Не удалось переименовать автора %s в %s", oldName.toString(), newName.toString()));

        try {
            return authorDao.getById(authorOptional.map(Author::getId)
                    .orElseThrow(()-> new AppServiceException(String.format("Не удалось найти автора: %s", oldName.toString()))));
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Author> getAll() throws AppServiceException {
        final List<Author> authorList;
        try {
            authorList = authorDao.readAll();
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
        return authorList;
    }

    @Override
    public Optional<Author> findByFullName(FullName fullName) throws AppServiceException {
        try {
            return authorDao.findByFullName(fullName);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }
}
