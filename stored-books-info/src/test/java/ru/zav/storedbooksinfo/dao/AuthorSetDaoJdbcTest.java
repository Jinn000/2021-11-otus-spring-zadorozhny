package ru.zav.storedbooksinfo.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.AuthorSet;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.UuidGeneratorNoDashes;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Проверка DAO работы с AuthorSet:")
@Import({AuthorDaoJdbc.class, AuthorSetDaoJdbc.class})
@JdbcTest
class AuthorSetDaoJdbcTest {

    private final AuthorDao authorDao;
    private final AuthorSetDao authorSetDao;

    @Autowired
    AuthorSetDaoJdbcTest(AuthorDao authorDao, AuthorSetDao authorSetDao) {
        this.authorDao = authorDao;
        this.authorSetDao = authorSetDao;
    }

    @DisplayName("Проверка получения AuthorSet по ID")
    @Test
    void shouldCorrectGetById() throws AppDaoException {
        // Существующий в базе с рождения - 'ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Author expectedAuthor = authorDao.getById("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        final AuthorSet expectedAuthorSet = new AuthorSet("ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10","ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", expectedAuthor);
        final AuthorSet actualAuthorSet = authorSetDao.getById(expectedAuthorSet.getId());

        assertThat(actualAuthorSet).usingRecursiveComparison().isEqualTo(expectedAuthorSet);
    }

    @DisplayName("Проверка удаления AuthorSet по ID")
    @Test
    void shouldCorrectDeleteById() throws AppDaoException {
        // Существующий в базе с рождения - 'ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Author author = authorDao.getById("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        final AuthorSet deletedAuthorSet = new AuthorSet("ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10","ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", author);

        authorSetDao.deleteById(deletedAuthorSet.getId());
        final AuthorSet actualAuthorSet = authorSetDao.getById(deletedAuthorSet.getId());

        assertThat(actualAuthorSet).isNull();
    }

    @DisplayName("Проверка способности добавлять AuthorSet.")
    @Test
    void shouldCorrectInsert() throws AppDaoException {
        final String authorSetId = new UuidGeneratorNoDashes().generateUuid();
        final Author author1 = authorDao.getById("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        final AuthorSet expectedAuthorSet = new AuthorSet(new UuidGeneratorNoDashes().generateUuid(),authorSetId, author1);
        authorSetDao.insert(expectedAuthorSet);
        final AuthorSet actualAuthorSet = authorSetDao.getById(expectedAuthorSet.getId());

        assertThat(actualAuthorSet).usingRecursiveComparison().isEqualTo(expectedAuthorSet);
    }

    @DisplayName("Проверка получения всех AuthorSet.")
    @Test
    void shouldCorrectReadAll() throws AppDaoException {
        final List<AuthorSet> authorSetList = authorSetDao.readAll();
        assertThat(authorSetList.size()).isEqualTo(3);

        // Существующий в базе с рождения - 'ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final String id = "ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
        final String authorSetId = "ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10";
        final Author expectedAuthor = authorDao.getById("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        final AuthorSet expectedAuthorSet = new AuthorSet(id, authorSetId, expectedAuthor);
        final Optional<AuthorSet> authorSetOptional = authorSetList.stream().filter(a -> a.getId().equals(id)).findFirst();

        assertThat(authorSetOptional.isPresent()).isTrue();
        assertThat(authorSetOptional.get()).usingRecursiveComparison().isEqualTo(expectedAuthorSet);
    }

    @DisplayName("Проверка получения AuthorSet по AuthorId")
    @Test
    void shouldCorrectFindByAuthorId() throws AppDaoException {
        // Существующий в базе с рождения - 'ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Author expectedAuthor = authorDao.getById("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        final AuthorSet expectedAuthorSet = new AuthorSet("ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10","ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", expectedAuthor);

        final List<AuthorSet> authorSetFind = authorSetDao.findByAuthorId(expectedAuthor.getId());
        assertThat(authorSetFind.isEmpty()).isFalse();
        assertThat(authorSetFind.get(0)).usingRecursiveComparison().isEqualTo(expectedAuthorSet);

        //несуществующий не должен быть найден
        final List<AuthorSet> fakeAuthorSet = authorSetDao.findByAuthorId("fullNameFake");
        assertThat(fakeAuthorSet.isEmpty()).isTrue();
    }

    @DisplayName("Проверка получения AuthorSet по id")
    @Test
    void shouldCorrectFindByAuthorsSetId() throws AppDaoException {
        // Существующий в базе с рождения - 'ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10'
        final Author expectedAuthor = authorDao.getById("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
        final AuthorSet expectedAuthorSet = new AuthorSet("ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10","ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10", expectedAuthor);

        final List<AuthorSet> authorSetFind = authorSetDao.findByAuthorsSetId(expectedAuthorSet.getAuthorSetId());
        assertThat(authorSetFind.isEmpty()).isFalse();
        assertThat(authorSetFind.size() == 1).isTrue();
        assertThat(authorSetFind.get(0)).usingRecursiveComparison().isEqualTo(expectedAuthorSet);

        //несуществующий не должен быть найден
        final List<AuthorSet> fakeAuthorSet = authorSetDao.findByAuthorId("fullNameFake");
        assertThat(fakeAuthorSet.isEmpty()).isTrue();
    }
}