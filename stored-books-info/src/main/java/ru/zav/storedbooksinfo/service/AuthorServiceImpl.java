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
    public int delete(FullName fullName) throws AppServiceException {
        if(StringUtils.isBlank(fullName.getFirstName())) throw new AppServiceException("Ошибка! Не указано Имя автора.");
        if(StringUtils.isBlank(fullName.getLastName())) throw new AppServiceException("Ошибка! Не указано Отчество автора.");
        if(StringUtils.isBlank(fullName.getFamilyName())) throw new AppServiceException("Ошибка! Не указана Фамилия автора.");

        final Optional<Author> authorOptional;
        try {
            authorOptional = authorRepository.findByFullName(fullName);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        return authorOptional.map(Author::getId)
                .map(id -> {
                    try {
                        return authorRepository.deleteById(id);
                    } catch (AppDaoException e) {
                        return 0;
                    }
                }).orElse(0);
    }

    @Transactional
    @Override
    public Author rename(FullName oldName, FullName newName) throws AppServiceException {
        Optional<Author> authorOptional;
        try {
            authorOptional = authorRepository.findByFullName(oldName);
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
    public List<Author> getAll() throws AppServiceException {
        try {
            return authorRepository.readAll();
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Author> findByFullName(FullName fullName) throws AppServiceException {
        try {
            return authorRepository.findByFullName(fullName);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }
}
