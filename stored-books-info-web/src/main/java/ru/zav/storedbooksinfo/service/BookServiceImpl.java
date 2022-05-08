package ru.zav.storedbooksinfo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.dao.BookRepository;
import ru.zav.storedbooksinfo.datatypes.BookBean;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final GenreService genreService;


    @Override
    public Book add(BookBean bookBean) {
        try {
            final Optional<Genre> genreOptional = genreService.findByDescription(bookBean.getGenreTitle());
            final Genre genre = genreOptional.orElse(genreService.add(bookBean.getGenreTitle()));

            return bookRepository.save(new Book(UUID.randomUUID().toString(), bookBean.getTitle(), genre, bookBean.getAuthors(), bookBean.getComments()));
        } catch (AppDaoException e) {
            throw new AppServiceException(String.format("Не удалось добавить книгу %s. Причина: %s", bookBean.toString(), e.getLocalizedMessage()), e);
        }
    }

    @Override
    public int delete(String bookId){
        try {
            var deletedBooksCountOpt = Optional.ofNullable(bookRepository.getById(bookId)).map(Book::getId)
                    .map(id -> {
                        try {
                            bookRepository.deleteById(id);
                            return 1;
                        } catch (EmptyResultDataAccessException e) {
                            log.error(e.getLocalizedMessage());
                        }
                        return 0;
                    });
            return deletedBooksCountOpt.orElse(0);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Book changeTitle(String bookId, String newTitle) {
        final Optional<Book> bookOptional;
        try {
            bookOptional = Optional.ofNullable(bookRepository.getById(bookId));
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        Book updatedBook = bookOptional.map(book -> {
            book.setTitle(newTitle);
            return bookRepository.save(book);
        }).orElseThrow(() -> new AppServiceException(String.format("Не удалось найти книгу в ID: %s", bookId)));

        if(!updatedBook.getTitle().equals(newTitle)) throw new AppServiceException(String.format("Не удалось переименовать книгу с ID %s в %s", bookId, newTitle));

        try {
            return bookOptional.map(Book::getId)
                    .map(bookRepository::getById)
                    .orElseThrow(()-> new AppServiceException(String.format("Не удалось переименовать книгу с ID %s в %s", bookId, newTitle)));
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Book> getAll() {
        try {
            return bookRepository.findAll();
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Book> findByTitle(String title) {
        if(title == null) throw new AppServiceException("Ошибка! Не указано наименование книги для поиска.");

        try {
            return bookRepository.findByTitle(title);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }
}
