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


    @BeforeEach
    void beforeEach(){
        this.existedBookComment = new BookComment(EXISTED_COMMENT_ID_NIKOLAY, EXISTED_NAME, EXISTED_BOOK_ID, EXISTED_COMMENT);
        this.expectedBookComment = new BookComment(null, EXISTED_NAME, EXISTED_BOOK_ID, NEW_COMMENT);
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
}