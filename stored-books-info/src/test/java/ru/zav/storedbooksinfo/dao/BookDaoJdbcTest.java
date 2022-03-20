package ru.zav.storedbooksinfo.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.AuthorSet;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.UuidGeneratorNoDashes;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Проверка DAO работы с Книгами:")
@Import({BookDaoJdbc.class, GenreDaoJdbc.class, AuthorSetDaoJdbc.class, AuthorDaoJdbc.class})
@JdbcTest
class BookDaoJdbcTest {
    private final BookDao bookDao;

    @Autowired
    BookDaoJdbcTest(BookDao bookDao) {
        this.bookDao = bookDao;
    }


    @DisplayName("Проверка получения Книги по ID.")
    @Test
    void shouldCorrectGetById() throws AppDaoException {
        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Optional<Book> expectedBookOpt = bookDao.getById("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        assertThat(expectedBookOpt.isPresent());
        final Optional<Book> actualBookOpt = expectedBookOpt.map(Book::getId).flatMap(bookDao::getById);

        assertThat(actualBookOpt.isPresent());
        assertThat(actualBookOpt.get()).usingRecursiveComparison().isEqualTo(expectedBookOpt.get());
    }

    @DisplayName("Проверка удаления Книги по ID.")
    @Test
    void shouldCorrectDeleteById() throws AppDaoException {
        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Optional<Book> deletedBookOpt = bookDao.getById("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        assertThat(deletedBookOpt.isPresent());

        deletedBookOpt.map(Book::getId).map(bookDao::deleteById);
        final Optional<Book> actualBookOpt = deletedBookOpt.map(Book::getId).flatMap(bookDao::getById);
        assertThat(actualBookOpt.isPresent()).isFalse();

        deletedBookOpt.map(bookDao::insert);
    }

    @DisplayName("Проверка способности добавлять Книги.")
    @Test
    void shouldCorrectInsert() throws AppDaoException {
        final String bookId = new UuidGeneratorNoDashes().generateUuid();
        final Genre genre = new Genre("G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");
        final List<Author> authorList = List.of(new Author("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Николай", "Васильевич", "Гоголь"));
        final Book expectedBook = new Book(bookId, "Вечера на хуторе близ диканьки", genre, authorList);
        bookDao.insert(expectedBook);
        final Optional<Book> actualBook = bookDao.getById(expectedBook.getId());

        assertThat(actualBook.isPresent()).isTrue();
        assertThat(actualBook.get()).usingRecursiveComparison().isEqualTo(expectedBook);

        bookDao.deleteById(expectedBook.getId());
    }

    @DisplayName("Проверка получения всех Книг.")
    @Test
    void shouldCorrectReadAll() throws AppDaoException {
        final List<Book> bookList = bookDao.readAll();
        assertThat(bookList.size()).isEqualTo(2);

        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final String bookId = "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
        final Genre genre = new Genre("G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");
        final List<Author> authorList = List.of(new Author("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Николай", "Васильевич", "Гоголь"));
        final Book expectedBook = new Book(bookId, "Вечера на хуторе близ диканьки", genre, authorList);
        final Optional<Book> bookOpt = bookList.stream().filter(a -> a.getId().equals(bookId)).findFirst();

        assertThat(bookOpt.isPresent()).isTrue();
        assertThat(bookOpt.get()).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Проверка получения Книг по жанрам.")
    @Test
    void shouldCorrectFindByGenre() throws AppDaoException {
        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Optional<Book> expectedBook = bookDao.getById("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        assertThat(expectedBook.isPresent()).isTrue();
        final Genre genre = new Genre("G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");

        final List<Book> booksFind = bookDao.findByGenre(genre);
        assertThat(booksFind.isEmpty()).isFalse();
        assertThat(booksFind.get(0)).usingRecursiveComparison().isEqualTo(expectedBook.get());

        //несуществующий не должен быть найден
        final Genre fakeGenre = new Genre("G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10_FAKE","Мистика");
        final List<Book> fakeBooks = bookDao.findByGenre(fakeGenre);
        assertThat(fakeBooks.isEmpty()).isTrue();
    }

    @DisplayName("Проверка получения Книг по жанрам.")
    @Test
    void shouldCorrectFindByTitle() throws AppDaoException {
        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Optional<Book> expectedBook = bookDao.getById("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        assertThat(expectedBook.isPresent()).isTrue();

        final List<Book> booksFind = bookDao.findByTitle("Вечера на хуторе близ диканьки");
        assertThat(booksFind.isEmpty()).isFalse();
        assertThat(booksFind.get(0)).usingRecursiveComparison().isEqualTo(expectedBook.get());

        //несуществующий не должен быть найден
        final List<Book> fakeBooks = bookDao.findByTitle("Вечера на хуторе близ диВаньки");
        assertThat(fakeBooks.isEmpty()).isTrue();
    }

    @DisplayName("Проверка получения AuthorSetId по ID книги.")
    @Test
    void shouldCorrectFindAuthorSetIdByBookId() throws AppDaoException {
        // Существующий в базе с рождения - 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final AuthorSet expectedAuthorSet = new AuthorSet("ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10", "ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", new Author("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Николай", "Васильевич", "Гоголь"));

        final Optional<String> authorSetIdFindOpt = bookDao.findAuthorSetIdByBookId("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        assertThat(authorSetIdFindOpt.isPresent()).isTrue();
        assertThat(authorSetIdFindOpt.get()).isEqualTo(expectedAuthorSet.getAuthorSetId());

        //несуществующий не должен быть найден
        final Optional<String> authorSetIdFindFakeOpt = bookDao.findAuthorSetIdByBookId("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10_FAKE");
        assertThat(authorSetIdFindFakeOpt.isEmpty()).isTrue();
    }

/*    @DisplayName("Проверка очистки таблицы с Книгами.")
    @Test
    void shouldCorrectClearAll() throws AppDaoException {
        final List<Book> storeBookList = bookDao.readAll();

        bookDao.clearAll();
        final List<Book> actualBookList = bookDao.readAll();
        assertThat(actualBookList.isEmpty()).isTrue();

        //восстановить данные
        storeBookList.forEach(book -> {
            try {
                bookDao.insert(book);
            } catch (AppDaoException e) {
                log.error(e.getLocalizedMessage());
            }
        });
    }*/

    @Test
    void clearAll() {
    }
}