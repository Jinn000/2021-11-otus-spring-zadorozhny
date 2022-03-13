package ru.zav.storedbooksinfo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zav.storedbooksinfo.dao.AuthorDao;
import ru.zav.storedbooksinfo.dao.AuthorSetDao;
import ru.zav.storedbooksinfo.dao.BookDao;
import ru.zav.storedbooksinfo.dao.GenreDao;
import ru.zav.storedbooksinfo.datatypes.BookBean;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.AppServiceException;
import ru.zav.storedbooksinfo.utils.UuidGeneratorNoDashes;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final GenreDao genreDao;
    private final AuthorDao authorDao;
    private final AuthorSetDao authorSetDao;

    private final GenreService genreService;


    @Override
    public Book add(@NotNull BookBean bookBean) throws AppServiceException {
        final Optional<Genre> genreOptional;
        try {
            genreOptional = genreDao.findByDescription(bookBean.getGenreTitle());
        } catch (AppDaoException e) {
            throw new AppServiceException(String.format("Не удалось добавить книгу %s. Причина: %s", bookBean.toString(), e.getLocalizedMessage()), e);
        }
        Genre genre = genreOptional.isPresent() ? genreOptional.get() : genreService.add(bookBean.getGenreTitle());


        final Book newBook;
        try {
            newBook = new Book(new UuidGeneratorNoDashes().generateUuid(), bookBean.getTitle(), genre, bookBean.getAuthors());
            bookDao.insert(newBook);
        } catch (AppDaoException e) {
            throw new AppServiceException(String.format("Не удалось добавить книгу %s. Причина: %s", bookBean.toString(), e.getLocalizedMessage()), e);
        }
        return newBook;
    }

    @Override
    public int delete(@NotNull String bookId) throws AppServiceException {
        try {
            var deletedBooksCountOpt = bookDao.getById(bookId).map(Book::getId)
                    .map(id -> {
                        try {
                            return bookDao.deleteById(id);
                        } catch (AppDaoException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    });
            return deletedBooksCountOpt.orElse(0);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Book changeTitle(String bookId, String newTitle) throws AppServiceException {
        final Optional<Book> bookOptional;
        try {
            bookOptional = bookDao.getById(bookId);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }

        var updatedCount = bookOptional.map(book -> {
            final Book newBook = new Book(book.getId(), newTitle, book.getGenre(), book.getAuthors());
            try {
                return bookDao.insert(newBook);
            } catch (AppDaoException e) {
                e.printStackTrace();
            }
            return 0;
        }).orElseThrow(() -> new AppServiceException(String.format("Не удалось найти книгу в ID: %s", bookId)));

        if(updatedCount.equals(0)) throw new AppServiceException(String.format("Не удалось переименовать книгу с ID %s в %s", bookId, newTitle));

        try {
            return bookDao.getById(bookOptional.map(Book::getId)
                    .orElseThrow(()-> new AppServiceException(String.format("Не удалось найти книгу c ID: %s", bookId)))
            ).orElseThrow(()-> new AppServiceException(String.format("Не удалось переименовать книгу с ID %s в %s", bookId, newTitle)));
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Book> getAll() throws AppServiceException {
        final List<Book> bookList;
        try {
            bookList = bookDao.readAll();
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
        return bookList;
    }

    @Override
    public List<Book> findByTitle(String title) throws AppServiceException {
        if(title == null) throw new AppServiceException("Ошибка! Не указано наименование книги для поиска.");

        final List<Book> bookList;
        try {
            bookList = bookDao.findByTitle(title);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
        return bookList;
    }
}
