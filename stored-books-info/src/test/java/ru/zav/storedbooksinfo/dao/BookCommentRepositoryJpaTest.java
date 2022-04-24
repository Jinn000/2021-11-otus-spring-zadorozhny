package ru.zav.storedbooksinfo.dao;

import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.BookComment;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверка DAO работы с Комментариями:")
@Data
@Import({BookCommentRepositoryJpa.class, BookRepositoryJpa.class})
@DataJpaTest
class BookCommentRepositoryJpaTest {
    @Autowired
    private BookCommentRepository bookCommentRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestEntityManager em;

    private BookComment expectedBookComment;

    public static final String EXPECTED_COMMENT_ID_NIKOLAY = "BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
    public static final String EXPECTED_BOOK_ID = "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
    public static final String EXPECTED_COMMENT = "Not bad.";
    public static final String EXPECTED_NAME = "Николай";

    @BeforeEach
    void beforeEach(){
        this.expectedBookComment = new BookComment(EXPECTED_COMMENT_ID_NIKOLAY, EXPECTED_NAME, em.find(Book.class, EXPECTED_BOOK_ID), EXPECTED_COMMENT);
    }

    @DisplayName("Проверка получения Комментария по ID")
    @Test
    void shouldCorrectGetById() {
        final BookComment actualBookComment = bookCommentRepository.getById(EXPECTED_COMMENT_ID_NIKOLAY);
        assertThat(actualBookComment).usingRecursiveComparison().isEqualTo(this.expectedBookComment);
    }

    @DisplayName("Проверка удаления Комментария по ID")
    @Test
    void shouldCorrectDeleteById() {
        bookCommentRepository.deleteById(EXPECTED_COMMENT_ID_NIKOLAY);
        final BookComment actualBookComment = bookCommentRepository.getById(EXPECTED_COMMENT_ID_NIKOLAY);
        assertThat(actualBookComment).isNull();
    }

    @DisplayName("Проверка способности добавлять комментарий.")
    @Test
    void shouldCorrectInsert() {
        BookComment expectedBookComment = new BookComment(null, EXPECTED_NAME, em.find(Book.class, EXPECTED_BOOK_ID), EXPECTED_COMMENT);
        expectedBookComment = bookCommentRepository.save(expectedBookComment);
        final BookComment actualBookComment = bookCommentRepository.getById(expectedBookComment.getId());
        assertThat(actualBookComment).usingRecursiveComparison().isEqualTo(expectedBookComment);
    }

    @DisplayName("Проверка способности изменить комментарий.")
    @Test
    void shouldCorrectUpdate() {
        bookCommentRepository.save(this.expectedBookComment);
        final BookComment actualBookComment = bookCommentRepository.getById(EXPECTED_COMMENT_ID_NIKOLAY);
        assertThat(actualBookComment).usingRecursiveComparison().isEqualTo(this.expectedBookComment);
    }

    @DisplayName("Проверка получения всех комментариев.")
    @Test
    void shouldCorrectReadAll() {
        final List<BookComment> bookCommentList = bookCommentRepository.readAll();
        assertThat(bookCommentList.size()).isEqualTo(2);

        final Optional<BookComment> bookCommentOptional = bookCommentList.stream().filter(a -> a.getId().equals(EXPECTED_COMMENT_ID_NIKOLAY)).findFirst();
        assertThat(bookCommentOptional.isPresent()).isTrue();
        assertThat(bookCommentOptional.get()).usingRecursiveComparison().isEqualTo(this.expectedBookComment);
    }

    @DisplayName("Проверка получения Комментариев по ID книги")
    @Test
    void shouldCorrectFindByBookId() {
        final Optional<List<BookComment>> commentListOpt = bookRepository.getById(EXPECTED_BOOK_ID).map(Book::getComments);
        assertThat(commentListOpt.isEmpty()).isFalse();

        final List<BookComment> bookCommentByBookId = commentListOpt.orElse(new ArrayList<>());
        assertThat(bookCommentByBookId.isEmpty()).isFalse();
        assertThat(bookCommentByBookId.get(0)).usingRecursiveComparison().isEqualTo(this.expectedBookComment);

        //несуществующий не должен быть найден
        final List<BookComment> fakeBookCommentByBookId = bookRepository.getById("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A99").map(Book::getComments).orElse(new ArrayList<>());
        assertThat(fakeBookCommentByBookId.isEmpty()).isTrue();
    }
}