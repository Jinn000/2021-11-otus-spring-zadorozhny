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
    @Override
    public Optional<Book> addComment(String bookId, String comment) {
        final Optional<Book> optionalBook = Optional.ofNullable(bookRepository.getById(bookId));

        optionalBook.map(Book::getComments)
                .map(list-> list.add(new BookComment(null, CURRENT_USER_NAME, optionalBook.orElse(null), comment)));

        return optionalBook.map(Book::getId).map(bookRepository::getById);
    }

    @Override
    public Optional<Book> deleteComment(String commentId) {

        var bookOpt = Optional.ofNullable(bookCommentRepository.getById(commentId))
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
        final Optional<BookComment> bookCommentOptional = Optional.ofNullable(bookCommentRepository.getById(commentId));

        return bookCommentOptional
                .map(c -> {
                    c.setComment(newComment);
                    return c;
                })
                .map(bookCommentRepository::save)
                .map(BookComment::getBook);
    }

    @Override
    public List<BookComment> readComments(String bookId) {
        return Optional.ofNullable(bookRepository.getById(bookId)).map(Book::getComments).orElse(new ArrayList<>());
    }
}
