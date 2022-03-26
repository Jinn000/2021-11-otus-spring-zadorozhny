package ru.zav.storedbooksinfo.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.AuthorSet;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Проверка DAO работы с AuthorSet:")
@Import({AuthorRepositoryJpa.class, AuthorSetRepositoryJpa.class})
@DataJpaTest
class AuthorSetRepositoryJpaTest {
    public static final String EXISTED_AUTHOR_ID_GOGOL = "A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
    public static final String EXISTED_AUTHOR_SET_ID = "ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
    public static final String EXISTED_AS_ID = "ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10";

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorSetRepository authorSetRepository;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Проверка получения AuthorSet по ID")
    @Test
    void shouldCorrectGetById() throws AppDaoException {
        // Существующий в базе с рождения - 'ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Author expectedAuthor = authorRepository.getById(EXISTED_AUTHOR_ID_GOGOL);
        final AuthorSet expectedAuthorSet = new AuthorSet(EXISTED_AS_ID, EXISTED_AUTHOR_SET_ID, expectedAuthor);
        final AuthorSet actualAuthorSet = authorSetRepository.getById(expectedAuthorSet.getId());

        assertThat(actualAuthorSet).usingRecursiveComparison().isEqualTo(expectedAuthorSet);
    }

    @DisplayName("Проверка удаления AuthorSet по ID")
    @Test
    void shouldCorrectDeleteById() throws AppDaoException {
        // Существующий в базе с рождения - 'ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Author author = authorRepository.getById(EXISTED_AUTHOR_ID_GOGOL);
        final AuthorSet deletedAuthorSet = new AuthorSet(EXISTED_AS_ID,EXISTED_AUTHOR_SET_ID, author);

        authorSetRepository.deleteById(deletedAuthorSet.getId());
        final AuthorSet actualAuthorSet = authorSetRepository.getById(deletedAuthorSet.getId());

        assertThat(actualAuthorSet).isNull();
    }

    @DisplayName("Проверка способности добавлять AuthorSet.")
    @Test
    void shouldCorrectInsert() throws AppDaoException {
        final String authorSetId = UUID.randomUUID().toString();
        final Author author1 = authorRepository.getById(EXISTED_AUTHOR_ID_GOGOL);
        final AuthorSet expectedAuthorSet = new AuthorSet(null,authorSetId, author1);

        assertThat(authorSetRepository.save(expectedAuthorSet)).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedAuthorSet);
    }

    @DisplayName("Проверка получения всех AuthorSet.")
    @Test
    void shouldCorrectReadAll() throws AppDaoException {
        final List<AuthorSet> authorSetList = authorSetRepository.readAll();
        assertThat(authorSetList.size()).isEqualTo(3);

        // Существующий в базе с рождения - 'ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Author expectedAuthor = authorRepository.getById(EXISTED_AUTHOR_ID_GOGOL);
        final AuthorSet expectedAuthorSet = new AuthorSet(EXISTED_AS_ID, EXISTED_AUTHOR_SET_ID, expectedAuthor);
        final Optional<AuthorSet> authorSetOptional = authorSetList.stream().filter(a -> a.getId().equals(EXISTED_AS_ID)).findFirst();

        assertThat(authorSetOptional.isPresent()).isTrue();
        assertThat(authorSetOptional.get()).usingRecursiveComparison().isEqualTo(expectedAuthorSet);
    }

    @DisplayName("Проверка получения AuthorSet по AuthorId")
    @Test
    void shouldCorrectFindByAuthorId() throws AppDaoException {
        // Существующий в базе с рождения - 'ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Author expectedAuthor = authorRepository.getById(EXISTED_AUTHOR_ID_GOGOL);
        final AuthorSet expectedAuthorSet = new AuthorSet(EXISTED_AS_ID, EXISTED_AUTHOR_SET_ID, expectedAuthor);

        final List<AuthorSet> authorSetFind = authorSetRepository.findByAuthorId(expectedAuthor.getId());
        assertThat(authorSetFind.isEmpty()).isFalse();
        assertThat(authorSetFind.get(0)).usingRecursiveComparison().isEqualTo(expectedAuthorSet);

        //несуществующий не должен быть найден
        final List<AuthorSet> fakeAuthorSet = authorSetRepository.findByAuthorId(UUID.randomUUID().toString());
        assertThat(fakeAuthorSet.isEmpty()).isTrue();
    }

    @DisplayName("Проверка получения AuthorSet по id")
    @Test
    void shouldCorrectFindByAuthorsSetId() throws AppDaoException {
        // Существующий в базе с рождения - 'ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Author expectedAuthor = authorRepository.getById(EXISTED_AUTHOR_ID_GOGOL);
        final AuthorSet expectedAuthorSet = new AuthorSet(EXISTED_AS_ID, EXISTED_AUTHOR_SET_ID, expectedAuthor);

        final List<AuthorSet> authorSetFind = authorSetRepository.findByAuthorsSetId(expectedAuthorSet.getAuthorsSetId());
        assertThat(authorSetFind.isEmpty()).isFalse();
        assertThat(authorSetFind.size() == 1).isTrue();
        assertThat(authorSetFind.get(0)).usingRecursiveComparison().isEqualTo(expectedAuthorSet);

        //несуществующий не должен быть найден
        final List<AuthorSet> fakeAuthorSet = authorSetRepository.findByAuthorId(UUID.randomUUID().toString());
        assertThat(fakeAuthorSet.isEmpty()).isTrue();
    }
}