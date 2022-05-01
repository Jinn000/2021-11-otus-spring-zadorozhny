package ru.zav.storedbooksinfo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.dao.BookCommentRepository;
import ru.zav.storedbooksinfo.dao.BookRepository;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.BookComment;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentRepository bookCommentRepository;
    private final BookRepository bookRepository;

    /**Эмуляция залогиненого юзера*/
    public static final String CURRENT_USER_NAME = "Гость";

    //-----  Работа с коментами  --------------------------------------------------
    @Transactional
    @Override
    public Optional<Book> addComment(String bookId, String comment) {
        final Optional<Book> optionalBook = bookRepository.findById(bookId);

        optionalBook.map(Book::getComments)
                .map(list-> list.add(new BookComment(null, CURRENT_USER_NAME, optionalBook.orElse(null), comment)));

        return optionalBook.map(bookRepository::save).map(Book::getId).flatMap(bookRepository::findById);
    }

    @Transactional
    @Override
    public Optional<Book> deleteComment(String commentId) {

        var bookOpt = bookCommentRepository.findById(commentId)
                .map(BookComment::getBook);

        try {
            List<BookComment> bookComments = bookOpt.map(Book::getComments).orElse(new ArrayList<>());
            bookComments.stream()
                    .filter(c-> c.getId().equals(commentId))
                    .findFirst().map(bookComments::remove);
        } catch (Exception e) {
            throw new AppServiceException(e.getMessage(), e);
        }
        return bookOpt;
    }

    @Transactional
    @Override
    public Optional<Book> updateComment(String commentId, String newComment) {
        final Optional<BookComment> bookCommentOptional = bookCommentRepository.findById(commentId);

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
    public List<BookComment> readComments(String bookId) {
        return bookRepository.findById(bookId).map(Book::getComments).orElse(new ArrayList<>());
    }
}
