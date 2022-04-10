package ru.zav.storedbooksinfo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.dao.BookCommentRepository;
import ru.zav.storedbooksinfo.dao.BookRepository;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.BookComment;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentRepository bookCommentRepository;
    private final BookRepository bookRepository;

    /**Эмуляция залогиненого юзера*/
    private static final String CURRENT_USER_NAME = "Гость";

    //-----  Работа с коментами  --------------------------------------------------
    @Transactional
    @Override
    public Optional<Book> addComment(String bookId, String comment) throws AppServiceException {
        final Optional<Book> optionalBook = bookRepository.getById(bookId);
        var g =  optionalBook
                .map(book-> bookCommentRepository.save(new BookComment(null, CURRENT_USER_NAME, book, comment)));

        return optionalBook.map(Book::getId).flatMap(bookRepository::getById);
    }

    @Transactional
    @Override
    public Optional<Book> deleteComment(String commentId) throws AppServiceException {

        var bookOpt = Optional.ofNullable(bookCommentRepository.getById(commentId))
                .map(BookComment::getBook);

        try {
            bookCommentRepository.deleteById(commentId);
        } catch (Exception e) {
            throw new AppServiceException(e.getMessage(), e);
        }
        return bookOpt;
    }

    @Transactional
    @Override
    public Optional<Book> updateComment(String commentId, String newComment) throws AppServiceException {
        final Optional<BookComment> bookCommentOptional = Optional.ofNullable(bookCommentRepository.getById(commentId));

        return bookCommentOptional
                .map(c -> {
                    c.setComment(newComment);
                    return c;
                })
                .map(bookCommentRepository::save)
                .map(BookComment::getBook);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookComment> readComments(String bookId) throws AppServiceException {
        return bookRepository.getById(bookId).map(Book::getComments).orElse(null);
    }
}
