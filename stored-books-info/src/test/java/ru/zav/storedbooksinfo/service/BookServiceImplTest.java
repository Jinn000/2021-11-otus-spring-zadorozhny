package ru.zav.storedbooksinfo.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.BookComment;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Проверка работы с книгами:")
@Slf4j
@Data
@RequiredArgsConstructor
@Import(BookServiceImpl.class)
@SpringBootTest
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    private BookComment existedBookComment;
    private BookComment expectedBookComment;

    public static final String EXISTED_COMMENT_ID_NIKOLAY = "BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
    public static final String EXISTED_BOOK_ID = "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
    public static final String EXISTED_COMMENT = "Not bad.";
    public static final String EXISTED_NAME = "Николай";
    public static final String NEW_COMMENT = "New test comment.";
    public static final String EXISTED_BOOK_TITLE = "Вечера на хуторе близ диканьки";
    Book existedBook = null;


    @BeforeEach
    void beforeEach() throws AppServiceException {
        this.existedBook = bookService.findByTitle(EXISTED_BOOK_TITLE).get(0);
        this.existedBookComment = new BookComment(EXISTED_COMMENT_ID_NIKOLAY, EXISTED_NAME, this.existedBook, EXISTED_COMMENT);
        this.expectedBookComment = new BookComment(null, EXISTED_NAME, this.existedBook, NEW_COMMENT);
    }

    @Test
    @Transactional
    void shouldCorrectAddComment() throws AppServiceException {
        final Book book = bookService.findByTitle("Вечера на хуторе близ диканьки").get(0);

        final Optional<Book> actualBookOpt = bookService.addComment(book.getId(), NEW_COMMENT);
        assertThat(actualBookOpt.isPresent()).isTrue();
        assertThat(actualBookOpt.get().getComments().size()).isEqualTo(3);

        Optional<BookComment> bookCommentOpt = actualBookOpt.map(Book::getComments).map(list-> list.get(list.size()-1));
        assertThat(bookCommentOpt.isPresent()).isTrue();

        assertThat(bookCommentOpt.get()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBookComment);
    }

    @Test
    @Transactional
    void deleteComment() throws AppServiceException {
        final Optional<Book> optionalBook = bookService.deleteComment(EXISTED_COMMENT_ID_NIKOLAY);

        final List<Book> bookList = bookService.findByTitle(EXISTED_BOOK_TITLE);
        assertThat(bookList.isEmpty()).isFalse();

        final List<BookComment> bookComments = bookList.get(0).getComments();
        assertThat(bookComments.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void updateComment() throws AppServiceException {
        final String newCommentText = "newCommentText";
        final Optional<Book> bookOptional = bookService.updateComment(EXISTED_COMMENT_ID_NIKOLAY, newCommentText);

        final List<Book> bookList = bookService.findByTitle(EXISTED_BOOK_TITLE);
        assertThat(bookList.isEmpty()).isFalse();

        final List<BookComment> bookComments = bookList.get(0).getComments();
        assertThat(bookComments.size()).isEqualTo(2);
        assertThat(bookComments.get(0).getComment()).isEqualTo(newCommentText);
    }

    @Test
    @Transactional
    void readComments() throws AppServiceException {
        final List<BookComment> commentList = bookService.readComments(EXISTED_BOOK_ID);
        assertThat(commentList.size()).isEqualTo(2);
        assertThat(commentList.get(0)).isEqualTo(this.existedBookComment);
    }
}