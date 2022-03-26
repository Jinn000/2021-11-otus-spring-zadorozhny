package ru.zav.storedbooksinfo.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.zav.storedbooksinfo.dao.datatypes.EntityId;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Проверка DAO работы с Жанрами:")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
class GenreRepositoryJpaTest {
    @Autowired
    private GenreRepositoryJpa genreRepository;
    @Autowired
    private TestEntityManager em;


    @DisplayName("Проверка получения Жанра по ID.")
    @Test
    void shouldCorrectGetById() {
        // Существующий в базе с рождения - 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика'
        final Genre expectedGenre = new Genre("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");
        final Genre actualGenre = genreRepository.getById(new EntityId(expectedGenre.getId()));
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Проверка получения всех Жанров.")
    @Test
    void shouldCorrectReadAll() throws AppDaoException {
        final List<Genre> genreList = genreRepository.readAll();
        assertThat(genreList.size()).isEqualTo(3);

        // Существующий в базе с рождения - 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика'
        final Genre expectedGenre = new Genre("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");
        final Optional<Genre> genreOptional = genreList.stream().filter(a -> a.getId().equals("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10")).findFirst();
        assertThat(genreOptional.isPresent()).isTrue();
        assertThat(genreOptional.get()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }
    @DisplayName("Проверка удаления Жанра по ID.")
    @Test
    void shouldCorrectDeleteById() throws AppDaoException {
        // Существующий в базе с рождения - 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика'
        final Genre deletedGenre = new Genre("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");
        genreRepository.deleteById(new EntityId(deletedGenre.getId()));
        final Genre actualGenre = genreRepository.getById(new EntityId(deletedGenre.getId()));
        assertThat(actualGenre).isNull();
    }

    @DisplayName("Проверка способности добавлять Жанр.")
    @Test
    void shouldCorrectInsert() throws AppDaoException {
//        Genre expectedGenre = new Genre(null,"НовыйЖанр");
        Genre expectedGenre = genreRepository.getById(new EntityId("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10"));
        em.detach(expectedGenre);
        expectedGenre.setId(null);
        expectedGenre = genreRepository.save(expectedGenre);
        final Genre actualGenre = genreRepository.getById(new EntityId(expectedGenre.getId()));
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Проверка получения Автора по ФИО")
    @Test
    void shouldCorrectFindByDescription() throws AppDaoException {
        // Существующий в базе с рождения - 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика'
        final Genre expectedGenre = new Genre("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");

        final Optional<Genre> genreByDescription = genreRepository.findByDescription(expectedGenre.getDescription());
        assertThat(genreByDescription.isPresent()).isTrue();
        assertThat(genreByDescription.get()).usingRecursiveComparison().isEqualTo(expectedGenre);

        //несуществующий не должен быть найден
        final Optional<Genre> fakeGenre = genreRepository.findByDescription("fakeDescription");
        assertThat(fakeGenre.isPresent()).isFalse();
    }
}