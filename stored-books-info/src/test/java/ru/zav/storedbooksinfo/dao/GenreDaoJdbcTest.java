package ru.zav.storedbooksinfo.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.zav.storedbooksinfo.dao.datatypes.EntityId;
import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.UuidGeneratorNoDashes;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("Проверка DAO работы с Жанрами:")
@Import(GenreDaoJdbc.class)
@JdbcTest
class GenreDaoJdbcTest {

    private final GenreDao genreDao;

    @Autowired
    GenreDaoJdbcTest(GenreDaoJdbc genreDao) {
        this.genreDao = genreDao;
    }

    @DisplayName("Проверка получения Жанра по ID.")
    @Test
    void shouldCorrectGetById() throws AppDaoException {
        // Существующий в базе с рождения - 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика'
        final Genre expectedGenre = new Genre("G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");
        final Genre actualGenre = genreDao.getById(new EntityId(expectedGenre.getId()));
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }
    @DisplayName("Проверка удаления Жанра по ID.")
    @Test
    void shouldCorrectDeleteById() throws AppDaoException {
        // Существующий в базе с рождения - 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика'
        final Genre deletedGenre = new Genre("G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");
        genreDao.deleteById(new EntityId(deletedGenre.getId()));
        final Genre actualGenre = genreDao.getById(new EntityId(deletedGenre.getId()));
        assertThat(actualGenre).isNull();

        genreDao.insert(deletedGenre);
    }

    @DisplayName("Проверка способности добавлять Жанр.")
    @Test
    void shouldCorrectInsert() throws AppDaoException {
        final Genre expectedGenre = new Genre(new UuidGeneratorNoDashes().generateUuid(),"НовыйЖанр");
        genreDao.insert(expectedGenre);
        final Genre actualGenre = genreDao.getById(new EntityId(expectedGenre.getId()));
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);

        genreDao.deleteById(new EntityId(expectedGenre.getId()));
    }

    @DisplayName("Проверка получения всех Жанров.")
    @Test
    void shouldCorrectReadAll() throws AppDaoException {
        final List<Genre> genreList = genreDao.readAll();
        assertThat(genreList.size()).isEqualTo(3);

        // Существующий в базе с рождения - 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика'
        final Genre expectedGenre = new Genre("G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");
        final Optional<Genre> genreOptional = genreList.stream().filter(a -> a.getId().equals("G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10")).findFirst();
        assertThat(genreOptional.isPresent()).isTrue();
        assertThat(genreOptional.get()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

/*    @DisplayName("Проверка очистки таблицы с Жанрами.")
    @Test
    void shouldCorrectClearAll() throws AppDaoException {
        final List<Genre> storeGenreList = genreDao.readAll();

        genreDao.clearAll();
        final List<Genre> actualGenreList = genreDao.readAll();
        assertThat(actualGenreList.isEmpty()).isTrue();

        //восстановить данные
        storeGenreList.forEach(genre -> {
            try {
                genreDao.insert(genre);
            } catch (AppDaoException e) {
                log.error(e.getLocalizedMessage());
            }
        });
    }*/

    @DisplayName("Проверка получения Автора по ФИО")
    @Test
    void shouldCorrectFindByDescription() throws AppDaoException {
        // Существующий в базе с рождения - 'G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика'
        final Genre expectedGenre = new Genre("G0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");

        final Optional<Genre> genreByDescription = genreDao.findByDescription(expectedGenre.getDescription());
        assertThat(genreByDescription.isPresent()).isTrue();
        assertThat(genreByDescription.get()).usingRecursiveComparison().isEqualTo(expectedGenre);

        //несуществующий не должен быть найден
        final Optional<Genre> fakeGenre = genreDao.findByDescription("fakeDescription");
        assertThat(fakeGenre.isPresent()).isFalse();
    }
}