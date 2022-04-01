package ru.zav.storedbooksinfo.dao;

import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.BookComment;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Проверка DAO работы с Комментариями:")
@Data
@Import(BookCommentRepositoryJpa.class)
@DataJpaTest
class BookCommentRepositoryJpaTest {
    @Autowired
    private BookCommentRepository bookCommentRepository;
    @Autowired
    private TestEntityManager em;

    private BookComment expectedBookComment;

    public static final String EXPECTED_COMMENT_ID_NIKOLAY = "BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
    public static final String EXPECTED_BOOK_ID = "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
    public static final String EXPECTED_COMMENT = "Not bad.";
    public static final String EXPECTED_NAME = "Николай";

    @BeforeEach
    void beforeEach(){
        this.expectedBookComment = new BookComment(EXPECTED_COMMENT_ID_NIKOLAY, EXPECTED_NAME, EXPECTED_BOOK_ID, EXPECTED_COMMENT);
    }

    @DisplayName("Проверка получения Комментария по ID")
    @Test
    void shouldCorrectGetById() throws AppDaoException {
        final BookComment actualBookComment = bookCommentRepository.getById(EXPECTED_COMMENT_ID_NIKOLAY);
        assertThat(actualBookComment).usingRecursiveComparison().isEqualTo(this.expectedBookComment);
    }

    @DisplayName("Проверка удаления Комментария по ID")
    @Test
    void shouldCorrectDeleteById() throws AppDaoException {
        bookCommentRepository.deleteById(EXPECTED_COMMENT_ID_NIKOLAY);
        final BookComment actualBookComment = bookCommentRepository.getById(EXPECTED_COMMENT_ID_NIKOLAY);
        assertThat(actualBookComment).isNull();
    }

    @DisplayName("Проверка способности добавлять комментарий.")
    @Test
    void shouldCorrectInsert() throws AppDaoException {
        BookComment expectedBookComment = new BookComment(null, EXPECTED_NAME, EXPECTED_BOOK_ID, EXPECTED_COMMENT);
        expectedBookComment = bookCommentRepository.save(expectedBookComment);
        final BookComment actualBookComment = bookCommentRepository.getById(expectedBookComment.getId());
        assertThat(actualBookComment).usingRecursiveComparison().isEqualTo(expectedBookComment);
    }

    @DisplayName("Проверка способности изменить комментарий.")
    @Test
    void shouldCorrectUpdate() throws AppDaoException {
        bookCommentRepository.save(this.expectedBookComment);
        final BookComment actualBookComment = bookCommentRepository.getById(EXPECTED_COMMENT_ID_NIKOLAY);
        assertThat(actualBookComment).usingRecursiveComparison().isEqualTo(this.expectedBookComment);
    }

    @DisplayName("Проверка получения всех комментариев.")
    @Test
    void shouldCorrectReadAll() throws AppDaoException {
        final List<BookComment> bookCommentList = bookCommentRepository.readAll();
        assertThat(bookCommentList.size()).isEqualTo(2);

        final Optional<BookComment> bookCommentOptional = bookCommentList.stream().filter(a -> a.getId().equals(EXPECTED_COMMENT_ID_NIKOLAY)).findFirst();
        assertThat(bookCommentOptional.isPresent()).isTrue();
        assertThat(bookCommentOptional.get()).usingRecursiveComparison().isEqualTo(this.expectedBookComment);
    }

    @DisplayName("Проверка получения Комментариев по ID книги")
    @Test
    void shouldCorrectFindByBookId() throws AppDaoException {
        final List<BookComment> bookCommentByBookId = bookCommentRepository.findByBookId(EXPECTED_BOOK_ID);
        assertThat(bookCommentByBookId.isEmpty()).isFalse();
        assertThat(bookCommentByBookId.get(0)).usingRecursiveComparison().isEqualTo(this.expectedBookComment);

        //несуществующий не должен быть найден
        final List<BookComment> fakeBookCommentByBookId = bookCommentRepository.findByBookId("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A99");
        assertThat(fakeBookCommentByBookId.isEmpty()).isTrue();
    }
}