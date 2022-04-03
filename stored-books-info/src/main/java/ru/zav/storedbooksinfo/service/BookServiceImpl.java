package ru.zav.storedbooksinfo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.dao.BookCommentRepository;
import ru.zav.storedbooksinfo.dao.BookRepository;
import ru.zav.storedbooksinfo.dao.GenreRepository;
import ru.zav.storedbooksinfo.datatypes.BookBean;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.BookComment;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final BookCommentRepository bookCommentRepository;

    private final GenreService genreService;

    /**Эмуляция залогиненого юзера*/
    private static final String CURRENT_USER_NAME = "Гость";


    @Transactional
    @Override
    public Book add(BookBean bookBean) throws AppServiceException {
        try {
            final Optional<Genre> genreOptional = genreRepository.findByDescription(bookBean.getGenreTitle());
            final Genre genre = genreOptional.orElse(genreService.add(bookBean.getGenreTitle()));

            return bookRepository.save(new Book(UUID.randomUUID().toString(), bookBean.getTitle(), genre, bookBean.getAuthors(), bookBean.getComments()));
        } catch (AppDaoException e) {
            throw new AppServiceException(String.format("Не удалось добавить книгу %s. Причина: %s", bookBean.toString(), e.getLocalizedMessage()), e);
        }
    }

    @Transactional
    @Override
    public int delete(String bookId) throws AppServiceException{
        try {
            var deletedBooksCountOpt = bookRepository.getById(bookId).map(Book::getId)
                    .map(id -> {
                        try {
                            return bookRepository.deleteById(id);
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

    @Transactional
    @Override
    public Book changeTitle(String bookId, String newTitle) throws AppServiceException {
        final Optional<Book> bookOptional;
        try {
            bookOptional = bookRepository.getById(bookId);
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
                    .flatMap(bookRepository::getById)
                    .orElseThrow(()-> new AppServiceException(String.format("Не удалось переименовать книгу с ID %s в %s", bookId, newTitle)));
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public List<Book> getAll() throws AppServiceException {
        try {
            return bookRepository.readAll();
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public List<Book> findByTitle(String title) throws AppServiceException {
        if(title == null) throw new AppServiceException("Ошибка! Не указано наименование книги для поиска.");

        try {
            return bookRepository.findByTitle(title);
        } catch (AppDaoException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }
//-----  Работа с коментами  --------------------------------------------------
    @Transactional
    @Override
    public Optional<Book> addComment(String bookId, String comment) throws AppServiceException {
        final Optional<Book> optionalBook = bookRepository.getById(bookId);
        var g =  optionalBook.map(Book::getId)
                .map(bId-> bookCommentRepository.save(new BookComment(null, CURRENT_USER_NAME, bId, comment)));

        return optionalBook.map(Book::getId).flatMap(bookRepository::getById);
    }

    @Transactional
    @Override
    public Optional<Book> deleteComment(String commentId) throws AppServiceException {

        var bookIdOpt = Optional.ofNullable(bookCommentRepository.getById(commentId))
                .map(BookComment::getBookId)
                .flatMap(bookRepository::getById);

        try {
            bookCommentRepository.deleteById(commentId);
        } catch (Exception e) {
            throw new AppServiceException(e.getMessage(), e);
        }
        return bookIdOpt;
    }

    @Transactional
    @Override
    public Optional<Book> updateComment(String commentId, String newComment) throws AppServiceException {
        final Optional<BookComment> bookCommentOptional = Optional.ofNullable(bookCommentRepository.getById(commentId));

        return bookCommentOptional.stream()
                .peek(c-> c.setComment(newComment)).findFirst()
                .map(bookCommentRepository::save)
                .map(BookComment::getBookId)
                .flatMap(bookRepository::getById);
    }

    @Transactional
    @Override
    public List<BookComment> readComments(String bookId) throws AppServiceException {
        return bookRepository.getById(bookId).map(Book::getComments).orElse(null);
    }
}
