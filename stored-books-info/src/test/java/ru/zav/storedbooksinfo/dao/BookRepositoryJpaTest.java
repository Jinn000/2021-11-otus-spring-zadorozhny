package ru.zav.storedbooksinfo.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Проверка DAO работы с Книгами:")
@DataJpaTest
class BookRepositoryJpaTest {
    public static final String EXISTED_BOOK_ID = "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
    public static final String FAKE_EXISTED_BOOK_ID = "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10_FAKE";
    public static final String EXISTED_GENRE_ID_MYSTIC = "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10";
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Проверка получения Книг по жанрам.")
    @Transactional(readOnly = true)
    @Test
    void shouldCorrectFindByGenre() {
        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Optional<Book> expectedBook = bookRepository.findById(EXISTED_BOOK_ID);
        assertThat(expectedBook.isPresent()).isTrue();
        final Genre genre = new Genre(EXISTED_GENRE_ID_MYSTIC,"Мистика");

        final List<Book> booksFind = bookRepository.findByGenre(genre);
        assertThat(booksFind.isEmpty()).isFalse();
        assertThat(booksFind.get(0)).usingRecursiveComparison().isEqualTo(expectedBook.get());

        //несуществующий не должен быть найден
        final Genre fakeGenre = new Genre(FAKE_EXISTED_BOOK_ID,"Мистика");
        final List<Book> fakeBooks = bookRepository.findByGenre(fakeGenre);
        assertThat(fakeBooks.isEmpty()).isTrue();
    }

    @DisplayName("Проверка получения Книг по названию.")
    @Transactional(readOnly = true)
    @Test
    void shouldCorrectFindByTitle() {
        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Optional<Book> expectedBook = bookRepository.findById(EXISTED_BOOK_ID);
        assertThat(expectedBook.isPresent()).isTrue();

        final List<Book> booksFind = bookRepository.findByTitle("Вечера на хуторе близ диканьки");
        assertThat(booksFind.isEmpty()).isFalse();
        assertThat(booksFind.get(0)).usingRecursiveComparison().isEqualTo(expectedBook.get());

        //несуществующий не должен быть найден
        final List<Book> fakeBooks = bookRepository.findByTitle("Вечера на хуторе близ диВаньки");
        assertThat(fakeBooks.isEmpty()).isTrue();
    }
}