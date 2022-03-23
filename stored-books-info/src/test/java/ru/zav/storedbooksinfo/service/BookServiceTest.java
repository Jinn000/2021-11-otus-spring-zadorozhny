package ru.zav.storedbooksinfo.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.dao.BookDao;
import ru.zav.storedbooksinfo.datatypes.BookBean;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Тестирование сервиса работы с книгами:")
@SpringBootTest
class BookServiceTest {
    public static final String EXISTED_BOOK_TITLE = "Вечера на хуторе близ диканьки";
    public static final String NEW_BOOK_TITLE = "Новое название";
    private final BookService bookService;
    private final BookDao bookDao;

    @Setter @Getter
    private Book expectedBook;

    @Autowired
    public BookServiceTest(BookService bookService, BookDao bookDao) {
        this.bookService = bookService;
        this.bookDao = bookDao;
    }

    @BeforeEach
    private void beforeEach(){
        final String existedBookId = "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
        final Genre existedGenre = new Genre("G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");
        final List<Author> existedAuthorList = List.of(new Author("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Николай", "Васильевич", "Гоголь"));
        setExpectedBook(new Book(existedBookId, EXISTED_BOOK_TITLE, existedGenre, existedAuthorList));
    }


    @DisplayName("Проверка способности корректно добавлять книгу.")
    @Transactional
    @Test
    void shouldCorrectAdd() throws AppServiceException {
        final String newTitle = "Новая книга";
        final String newGenreTitle = "НовыйЖанр";
        final Author existAuthor = new Author("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", "Николай", "Васильевич", "Гоголь");

        BookBean bookBean = BookBean.builder()
                .title(newTitle)
                .genreTitle(newGenreTitle)
                .authors(List.of(existAuthor))
                .build();
        final Book createdBook = bookService.add(bookBean);
        assertThat(createdBook).isNotNull();
        final Optional<Book> foundBookOpt = bookDao.getById(createdBook.getId());

        assertThat(foundBookOpt.isPresent()).isTrue();
        assertThat(foundBookOpt.get().getTitle()).isEqualTo(newTitle);
        assertThat(foundBookOpt.get().getGenre().getDescription()).isEqualTo(newGenreTitle);
        assertThat(foundBookOpt.get().getAuthors().size()).isEqualTo(1);
        assertThat(foundBookOpt.get().getAuthors().get(0)).usingRecursiveComparison().isEqualTo(existAuthor);
    }

    @DisplayName("Проверка способности корректно удалять книгу.")
    @Transactional
    @Test
    void delete() throws AppServiceException {
        assertThat(bookDao.getById(expectedBook.getId()).isPresent()).isTrue();
        bookService.delete(expectedBook.getId());
        assertThat(bookDao.getById(expectedBook.getId()).isPresent()).isFalse();
    }

    @DisplayName("Проверка способности корректно изменять название книги.")
    @Transactional
    @Test
    void changeTitle() throws AppServiceException {
        assertThat(expectedBook.getTitle()).isEqualTo(EXISTED_BOOK_TITLE);
        bookService.changeTitle(expectedBook.getId(), NEW_BOOK_TITLE);

        final Optional<Book> optionalBook = bookDao.getById(expectedBook.getId());
        assertThat(optionalBook.isPresent()).isTrue();
        optionalBook.map(d-> assertThat(d.getTitle()).isEqualTo(NEW_BOOK_TITLE));
    }

    @DisplayName("Проверка способности корректно получать все книги.")
    @Test
    void shouldCorrectGetAll() throws AppServiceException {
        final List<Book> bookList = bookService.getAll();
        assertThat(bookList.size()).isEqualTo(2);

        final Optional<Book> bookOptional = bookList.stream().filter(d -> d.getId().equals(expectedBook.getId())).findFirst();
        assertThat(bookOptional.isPresent()).isTrue();
        bookOptional.map(d-> assertThat(d).usingRecursiveComparison().isEqualTo(expectedBook));
    }

    @DisplayName("Проверка способности корректно искать книги по названию.")
    @Test
    void findByTitle() throws AppServiceException {
        final List<Book> bookListByTitle = bookService.findByTitle(expectedBook.getTitle());
        assertThat(bookListByTitle.isEmpty()).isFalse();
        assertThat(bookListByTitle.size()).isEqualTo(1);
        assertThat(bookListByTitle.get(0)).usingRecursiveComparison().isEqualTo(expectedBook);
    }
}