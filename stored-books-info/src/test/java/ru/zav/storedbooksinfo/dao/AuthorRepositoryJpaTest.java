package ru.zav.storedbooksinfo.dao;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Проверка DAO работы с Авторами:")
@Data
@Import(AuthorRepositoryJpa.class)
@DataJpaTest
class AuthorRepositoryJpaTest {
    public static final String EXISTED_AUTHOR_ID_GOGOL = "A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private TestEntityManager em;


    @DisplayName("Проверка получения Автора по ID")
    @Test
    void shouldCorrectGetById() throws AppDaoException {
        // Существующий в базе с рождения - 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Николай', 'Васильевич', 'Гоголь'
        final Author expectedAuthor = new Author(EXISTED_AUTHOR_ID_GOGOL,"Николай", "Васильевич", "Гоголь");
        final Author actualPerson = authorRepository.getById(expectedAuthor.getId());
        assertThat(actualPerson).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Проверка удаления Автора по ID")
    @Test
    void shouldCorrectDeleteById() throws AppDaoException {
        // Существующий в базе с рождения - 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Николай', 'Васильевич', 'Гоголь'
        final Author deletedAuthor = new Author(EXISTED_AUTHOR_ID_GOGOL,"Николай", "Васильевич", "Гоголь");
        authorRepository.deleteById(EXISTED_AUTHOR_ID_GOGOL);
        final Author actualPerson = authorRepository.getById(deletedAuthor.getId());
        assertThat(actualPerson).isNull();
    }

    @DisplayName("Проверка способности добавлять автора.")
    @Test
    void shouldCorrectInsert() throws AppDaoException {
        Author expectedAuthor = new Author(null,"Николай", "Николаевич", "Николаев");
        expectedAuthor = authorRepository.save(expectedAuthor);
        final Author actualPerson = authorRepository.getById(expectedAuthor.getId());
        assertThat(actualPerson).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Проверка способности изменить данные Автора.")
    @Test
    void shouldCorrectUpdate() throws AppDaoException {
        final Author expectedAuthor = new Author(EXISTED_AUTHOR_ID_GOGOL,"Николай", "Васильевич", "Моголь");
        authorRepository.save(expectedAuthor);
        final Author actualPerson = authorRepository.getById(expectedAuthor.getId());
        assertThat(actualPerson).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Проверка получения всех Авторов.")
    @Test
    void shouldCorrectReadAll() throws AppDaoException {
        final List<Author> authorList = authorRepository.readAll();
        assertThat(authorList.size()).isEqualTo(3);

        // Существующий в базе с рождения - 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Николай', 'Васильевич', 'Гоголь'
        final Author expectedAuthor = new Author(EXISTED_AUTHOR_ID_GOGOL,"Николай", "Васильевич", "Гоголь");
        final Optional<Author> authorOptional = authorList.stream().filter(a -> a.getId().equals("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10")).findFirst();
        assertThat(authorOptional.isPresent()).isTrue();
        assertThat(authorOptional.get()).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

/*    @DisplayName("Проверка очистки таблицы с Авторами.")
    @Test
    void shouldCorrectClearAll() throws AppDaoException {
        final List<Author> storeAuthorList = authorDao.readAll();

        authorDao.clearAll();
        final List<Author> actualAuthorList = authorDao.readAll();
        assertThat(actualAuthorList.isEmpty()).isTrue();

        //восстановить данные
        storeAuthorList.forEach(author -> {
            try {
                authorDao.insert(author);
            } catch (AppDaoException e) {
                log.error(e.getLocalizedMessage());
            }
        });
    }*/

    @DisplayName("Проверка получения Автора по ФИО")
    @Test
    void shouldCorrectFindByFullName() throws AppDaoException {
        // Существующий в базе с рождения - 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Николай', 'Васильевич', 'Гоголь'
        final Author expectedAuthor = new Author(EXISTED_AUTHOR_ID_GOGOL,"Николай", "Васильевич", "Гоголь");
        final FullName fullName = FullName.builder()
                .firstName("Николай")
                .lastName("Васильевич")
                .familyName("Гоголь")
                .build();
        final Optional<Author> authorByFullName = authorRepository.findByFullName(fullName);
        assertThat(authorByFullName.isPresent()).isTrue();
        assertThat(authorByFullName.get()).usingRecursiveComparison().isEqualTo(expectedAuthor);

        //несуществующий не должен быть найден
        final FullName fullNameFake = FullName.builder()
                .firstName("01")
                .lastName("01")
                .familyName("01")
                .build();
        final Optional<Author> fakeAuthorByFullName = authorRepository.findByFullName(fullNameFake);
        assertThat(fakeAuthorByFullName.isPresent()).isFalse();
    }
}