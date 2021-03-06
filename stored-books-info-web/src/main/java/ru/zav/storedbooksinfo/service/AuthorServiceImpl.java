package ru.zav.storedbooksinfo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.dao.AuthorRepository;
import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public Author add(FullName fullName) {
        try {
            final Author newAuthor = new Author(null, fullName.getFirstName(), fullName.getLastName(), fullName.getFamilyName());
            return authorRepository.save(newAuthor);
        } catch (AppDaoException e) {
            throw new AppServiceException(String.format("Не удалось добавить автора %s. Причина: %s", fullName.toString(), e.getLocalizedMessage()), e);
        }
    }

    @Override
    public int delete(FullName fullName) {
        if(StringUtils.isBlank(fullName.getFirstName())) throw new AppServiceException("Ошибка! Не указано Имя автора.");
        if(StringUtils.isBlank(fullName.getLastName())) throw new AppServiceException("Ошибка! Не указано Отчество автора.");
        if(StringUtils.isBlank(fullName.getFamilyName())) throw new AppServiceException("Ошибка! Не указана Фамилия автора.");

        final Optional<Author> authorOptional;
        try {
            authorOptional = authorRepository.findByFirstNameAndLastNameAndFamilyName(fullName.getFirstName(), fullName.getLastName(), fullName.getFamilyName());
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        return authorOptional.map(Author::getId)
                .map(id -> {
                    try {
                        authorRepository.deleteById(id);
                        return 1;
                    } catch (EmptyResultDataAccessException e) {
                        log.error(e.getLocalizedMessage());
                    }
                    return 0;
                }).orElse(0);
    }

    @Transactional
    @Override
    public Author rename(FullName oldName, FullName newName) {
        Optional<Author> authorOptional;
        try {
            authorOptional = authorRepository.findByFirstNameAndLastNameAndFamilyName(oldName.getFirstName(), oldName.getLastName(), oldName.getFamilyName());
        } catch (AppDaoException e) {
            authorOptional = Optional.empty();
        }

        return authorOptional.map(author -> {
                author.setFirstName(newName.getFirstName());
                author.setLastName(newName.getLastName());
                author.setFamilyName(newName.getFamilyName());
                return author;
        }).orElseThrow(() -> new AppServiceException(String.format("Не удалось найти автора: %s", oldName.toString())));
    }

    @Override
    public List<Author> getAll() {
        try {
            return authorRepository.findAll();
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Author> findByFullName(FullName fullName) {
        try {
            return authorRepository.findByFirstNameAndLastNameAndFamilyName(fullName.getFirstName(), fullName.getLastName(), fullName.getFamilyName());
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }
}
