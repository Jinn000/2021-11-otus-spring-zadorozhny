package ru.zav.storedbooksinfo.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Проверка DAO работы с Авторами:")
@DataJpaTest
class AuthorRepositoryJpaTest {
    public static final String EXISTED_AUTHOR_ID_GOGOL = "A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Проверка получения Автора по ФИО")
    @Transactional(readOnly = true)
    @Test
    void shouldCorrectFindByFullName() {
        // Существующий в базе с рождения - 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Николай', 'Васильевич', 'Гоголь'
        final Author expectedAuthor = new Author(EXISTED_AUTHOR_ID_GOGOL,"Николай", "Васильевич", "Гоголь");
        final FullName fullName = FullName.builder()
                .firstName("Николай")
                .lastName("Васильевич")
                .familyName("Гоголь")
                .build();
        final Optional<Author> authorByFullName = authorRepository.findByFullName(fullName.getFirstName(), fullName.getLastName(), fullName.getFamilyName());
        assertThat(authorByFullName.isPresent()).isTrue();
        assertThat(authorByFullName.get()).usingRecursiveComparison().isEqualTo(expectedAuthor);

        //несуществующий не должен быть найден
        final FullName fullNameFake = FullName.builder()
                .firstName("01")
                .lastName("01")
                .familyName("01")
                .build();
        final Optional<Author> fakeAuthorByFullName = authorRepository.findByFullName(fullNameFake.getFirstName(), fullNameFake.getLastName(), fullNameFake.getFamilyName());
        assertThat(fakeAuthorByFullName.isPresent()).isFalse();
    }
}