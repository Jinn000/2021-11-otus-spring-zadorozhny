package ru.zav.storedbooksinfo.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.dao.AuthorRepository;
import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional
    @Override
    public Author add(FullName fullName) throws AppServiceException {
        try {
            final Author newAuthor = new Author(null, fullName.getFirstName(), fullName.getLastName(), fullName.getFamilyName());
            return authorRepository.save(newAuthor);
        } catch (AppDaoException e) {
            throw new AppServiceException(String.format("Не удалось добавить автора %s. Причина: %s", fullName.toString(), e.getLocalizedMessage()), e);
        }
    }

    @Transactional
    @Override
    public void delete(FullName fullName) throws AppServiceException {
        if(StringUtils.isBlank(fullName.getFirstName())) throw new AppServiceException("Ошибка! Не указано Имя автора.");
        if(StringUtils.isBlank(fullName.getLastName())) throw new AppServiceException("Ошибка! Не указано Отчество автора.");
        if(StringUtils.isBlank(fullName.getFamilyName())) throw new AppServiceException("Ошибка! Не указана Фамилия автора.");

        final Optional<Author> authorOptional;
        try {
            authorOptional = authorRepository.findByFullName(fullName);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        authorOptional.map(Author::getId)
                .map(id -> {
                    try {
                        return authorRepository.deleteById(id);
                    } catch (AppDaoException e) {
                        e.printStackTrace();
                    }
                    return 0;
                });
    }

    @Transactional
    @Override
    public Author rename(FullName oldName, FullName newName) throws AppServiceException {
        final Optional<Author> authorOptional;
        try {
            authorOptional = authorRepository.findByFullName(oldName);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        var updatedCount = authorOptional.map(author -> {
            try {
                final Author newAuthor = new Author(author.getId(), newName.getFirstName(), newName.getLastName(), newName.getFamilyName());
                return authorRepository.save(newAuthor);
            } catch (AppDaoException e) {
                e.printStackTrace();
            }
            return 0;
        }).orElseThrow(() -> new AppServiceException(String.format("Не удалось найти автора: %s", oldName.toString())));

        if(updatedCount.equals(0)) throw new AppServiceException(String.format("Не удалось переименовать автора %s в %s", oldName.toString(), newName.toString()));

        try {
            return authorRepository.getById(authorOptional.map(Author::getId)
                    .orElseThrow(()-> new AppServiceException(String.format("Не удалось найти автора: %s", oldName.toString()))));
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> getAll() throws AppServiceException {
        final List<Author> authorList;
        try {
            authorList = authorRepository.readAll();
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
        return authorList;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Author> findByFullName(FullName fullName) throws AppServiceException {
        try {
            return authorRepository.findByFullName(fullName);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }
}
