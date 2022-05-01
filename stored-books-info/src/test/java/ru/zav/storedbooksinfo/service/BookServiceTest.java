package ru.zav.storedbooksinfo.service;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.dao.AuthorRepository;
import ru.zav.storedbooksinfo.dao.BookRepository;
import ru.zav.storedbooksinfo.datatypes.BookBean;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.BookComment;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Тестирование сервиса работы с книгами:")
@Data
@SpringBootTest
class BookServiceTest {
    public static final String EXISTED_BOOK_TITLE = "Вечера на хуторе близ диканьки";
    public static final String NEW_BOOK_TITLE = "Новое название";
    public static final String EXISTED_BOOK_ID = "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
    public static final String FAKE_EXISTED_BOOK_ID = "G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10_FAKE";
    public static final String EXISTED_GENRE_ID_MYSTIC = "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10";
    Book existedBook = null;

    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Setter @Getter
    private Book expectedBook;


    @BeforeEach
    private void beforeEach() {
        final Genre existedGenre = new Genre(EXISTED_GENRE_ID_MYSTIC,"Мистика");
        this.existedBook = bookRepository.findById(EXISTED_BOOK_ID).orElseThrow(()-> new AppServiceException(String.format("Не удалось прочитать книгу с ID: %s", EXISTED_BOOK_ID)));
        final List<Author> existedAuthorList = List.of(new Author("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Николай", "Васильевич", "Гоголь"));
        final List<BookComment> existBookComments = Arrays.asList(new BookComment("BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", "Николай", this.existedBook, "Not bad.")
                , new BookComment("BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A11", "Сергей", this.existedBook, "Не читал, но осуждаю."));

        setExpectedBook(new Book(EXISTED_BOOK_ID, EXISTED_BOOK_TITLE, existedGenre, existedAuthorList, existBookComments));
    }


    @DisplayName("Проверка способности корректно добавлять книгу.")
    @Transactional
    @Test
    void shouldCorrectAdd() {
        final String newTitle = "Новая книга";
        final String newGenreTitle = "НовыйЖанр";
        final Optional<Author> existAuthorOpt = authorRepository.findById("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        assertThat(existAuthorOpt.isPresent()).isTrue();

        BookBean bookBean = BookBean.builder()
                .title(newTitle)
                .genreTitle(newGenreTitle)
                .authors(List.of(existAuthorOpt.get()))
                .comments(new ArrayList<>())
                .build();
        final Book createdBook = bookService.add(bookBean);
        assertThat(createdBook).isNotNull();
        final Optional<Book> foundBookOpt = bookRepository.findById(createdBook.getId());

        assertThat(foundBookOpt.isPresent()).isTrue();
        assertThat(foundBookOpt.get().getTitle()).isEqualTo(newTitle);
        assertThat(foundBookOpt.get().getGenre().getDescription()).isEqualTo(newGenreTitle);
        assertThat(foundBookOpt.get().getAuthors().size()).isEqualTo(1);
        assertThat(foundBookOpt.get().getAuthors().get(0)).usingRecursiveComparison().isEqualTo(existAuthorOpt);
    }

    @DisplayName("Проверка способности корректно удалять книгу.")
    @Transactional
    @Test
    void delete() {
        assertThat(bookRepository.findById(expectedBook.getId()).isEmpty()).isFalse();
        bookService.delete(expectedBook.getId());
        var isGone = bookService.getAll().stream()
                .map(Book::getId)
                .noneMatch(expectedBook.getId()::equals);
        assertThat(isGone).isTrue();
    }

    @DisplayName("Проверка способности корректно изменять название книги.")
    @Transactional
    @Test
    void changeTitle() {
        assertThat(expectedBook.getTitle()).isEqualTo(EXISTED_BOOK_TITLE);
        bookService.changeTitle(expectedBook.getId(), NEW_BOOK_TITLE);

        final Optional<Book> optionalBook = bookRepository.findById(expectedBook.getId());
        assertThat(optionalBook.isPresent()).isTrue();
        optionalBook.map(d-> assertThat(d.getTitle()).isEqualTo(NEW_BOOK_TITLE));
    }

    @DisplayName("Проверка способности корректно получать все книги.")
    @Transactional
    @Test
    void shouldCorrectGetAll() {
        final List<Book> bookList = bookService.getAll();
        assertThat(bookList.size()).isEqualTo(10);

        final Optional<Book> bookOptional = bookList.stream().filter(d -> d.getId().equals(expectedBook.getId())).findFirst();
        assertThat(bookOptional.isPresent()).isTrue();
        bookOptional.map(d-> assertThat(d).usingRecursiveComparison().isEqualTo(expectedBook));
    }

    @DisplayName("Проверка способности корректно искать книги по названию.")
    @Transactional
    @Test
    void findByTitle() {
        final List<Book> bookListByTitle = bookService.findByTitle(expectedBook.getTitle());
        assertThat(bookListByTitle.isEmpty()).isFalse();
        assertThat(bookListByTitle.size()).isEqualTo(1);
        assertThat(bookListByTitle.get(0)).usingRecursiveComparison().isEqualTo(expectedBook);
    }
}