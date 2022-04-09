package ru.zav.storedbooksinfo.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.BookComment;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Проверка DAO работы с Книгами:")
@DataJpaTest
@Import({BookRepositoryJpa.class, GenreRepositoryJpa.class, AuthorRepositoryJpa.class})
class BookRepositoryJpaTest {
    public static final String EXISTED_BOOK_ID = "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
    public static final String FAKE_EXISTED_BOOK_ID = "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10_FAKE";
    public static final String EXISTED_GENRE_ID_MYSTIC = "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10";
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestEntityManager em;


    @DisplayName("Проверка получения Книги по ID.")
    @Transactional(readOnly = true)
    @Test
    void shouldCorrectGetById() throws AppDaoException {
        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Genre genre = new Genre(EXISTED_GENRE_ID_MYSTIC,"Мистика");
        final List<Author> authorList = List.of(new Author("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Николай", "Васильевич", "Гоголь"));
        final List<BookComment> existBookComments = Arrays.asList(new BookComment("BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", "Николай", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", "Not bad.")
                , new BookComment("BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A11", "Сергей", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", "Не читал, но осуждаю."));

        final Book expectedBook = new Book(EXISTED_BOOK_ID, "Вечера на хуторе близ диканьки", genre, authorList, existBookComments);

        final Optional<Book> actualBookOpt = bookRepository.getById(EXISTED_BOOK_ID);

        assertThat(actualBookOpt.isPresent());
        assertThat(actualBookOpt.get()).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Проверка удаления Книги по ID.")
    @Transactional
    @Test
    void shouldCorrectDeleteById() throws AppDaoException {
        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Optional<Book> deletedBookOpt = bookRepository.getById(EXISTED_BOOK_ID);
        assertThat(deletedBookOpt.isPresent()).isTrue();

        deletedBookOpt.map(Book::getId).map(bookRepository::deleteById);

        final Optional<Book> actualBookOpt = deletedBookOpt.map(Book::getId).flatMap(bookRepository::getById);
        assertThat(actualBookOpt.isPresent()).isFalse();
    }

    @DisplayName("Проверка способности добавлять Книги.")
    @Transactional
    @Test
    void shouldCorrectInsert() throws AppDaoException {
        final Genre genre = new Genre(null,"Мистика");
        final List<Author> authorList = List.of(new Author(null,"Николай", "Васильевич", "Гоголь"));
        Book expectedBook = new Book(null, "Вечера на хуторе близ диканьки", genre, authorList, new ArrayList<>());
        em.detach(expectedBook);

        expectedBook = bookRepository.save(expectedBook);
        final Optional<Book> actualBook = bookRepository.getById(expectedBook.getId());

        assertThat(actualBook.isPresent()).isTrue();
        assertThat(actualBook.get()).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Проверка получения всех Книг.")
    @Transactional(readOnly = true)
    @Test
    void shouldCorrectReadAll() throws AppDaoException {
        final List<Book> bookList = bookRepository.readAll();
        assertThat(bookList.size()).isEqualTo(2);

        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Genre genre = new Genre(EXISTED_GENRE_ID_MYSTIC,"Мистика");
        final List<Author> authorList = List.of(new Author("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Николай", "Васильевич", "Гоголь"));
        final List<BookComment> existBookComments = Arrays.asList(new BookComment("BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", "Николай", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", "Not bad.")
                , new BookComment("BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A11", "Сергей", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", "Не читал, но осуждаю."));

        final Book expectedBook = new Book(EXISTED_BOOK_ID, "Вечера на хуторе близ диканьки", genre, authorList, existBookComments);
        final Optional<Book> bookOpt = bookList.stream().filter(a -> a.getId().equals(EXISTED_BOOK_ID)).findFirst();

        assertThat(bookOpt.isPresent()).isTrue();
        assertThat(bookOpt.get()).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Проверка получения Книг по жанрам.")
    @Transactional(readOnly = true)
    @Test
    void shouldCorrectFindByGenre() throws AppDaoException {
        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Optional<Book> expectedBook = bookRepository.getById(EXISTED_BOOK_ID);
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
    void shouldCorrectFindByTitle() throws AppDaoException {
        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Optional<Book> expectedBook = bookRepository.getById(EXISTED_BOOK_ID);
        assertThat(expectedBook.isPresent()).isTrue();

        final List<Book> booksFind = bookRepository.findByTitle("Вечера на хуторе близ диканьки");
        assertThat(booksFind.isEmpty()).isFalse();
        assertThat(booksFind.get(0)).usingRecursiveComparison().isEqualTo(expectedBook.get());

        //несуществующий не должен быть найден
        final List<Book> fakeBooks = bookRepository.findByTitle("Вечера на хуторе близ диВаньки");
        assertThat(fakeBooks.isEmpty()).isTrue();
    }
}