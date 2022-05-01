package ru.zav.storedbooksinfo.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.zav.storedbooksinfo.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Проверка DAO работы с Жанрами:")
@DataJpaTest
class GenreRepositoryJpaTest {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private TestEntityManager em;


    @DisplayName("Проверка получения Жанра по ID.")
    @Test
    void shouldCorrectGetById() {
        // Существующий в базе с рождения - 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика'
        final Genre expectedGenre = new Genre("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");
        final Optional<Genre> actualGenreOpt = genreRepository.findById(expectedGenre.getId());
        assertThat(actualGenreOpt.isPresent()).isTrue();
        assertThat(actualGenreOpt.get()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Проверка получения всех Жанров.")
    @Test
    void shouldCorrectReadAll() {
        final List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList.size()).isEqualTo(3);

        // Существующий в базе с рождения - 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика'
        final Genre expectedGenre = new Genre("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10","Мистика");
        final Optional<Genre> genreOptional = genreList.stream().filter(a -> a.getId().equals("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10")).findFirst();
        assertThat(genreOptional.isPresent()).isTrue();
        assertThat(genreOptional.get()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }
    @DisplayName("Проверка удаления Жанра по ID.")
    @Test
    void shouldCorrectDeleteById() {
        // Существующий в базе с рождения - 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика'
        final Optional<Genre> existedGenreOpt = genreRepository.findById("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10");
        assertThat(existedGenreOpt.isPresent()).isTrue();
        final int size = genreRepository.findAll().size();

        genreRepository.deleteById("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10");
        var isGone = genreRepository.findAll().stream()
                .map(Genre::getId)
                .noneMatch("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10"::equals);
        assertThat(isGone).isTrue();
        assertThat(genreRepository.findAll().size()).isEqualTo(size - 1);
    }

    @DisplayName("Проверка способности добавлять Жанр.")
    @Test
    void shouldCorrectInsert() {
        Optional<Genre> expectedGenreOpt = genreRepository.findById("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10");

        expectedGenreOpt.map(d-> {
            d.setId(null);
            return d;
        }).map(genreRepository::save);
        assertThat(expectedGenreOpt.isPresent()).isTrue();

        final Optional<Genre> actualGenre = genreRepository.findById("E181db60-9C0B-4EF8-BB6D-6BB9BD380A10");
        assertThat(actualGenre.isPresent()).isTrue();
        assertThat(actualGenre.get()).usingRecursiveComparison().isEqualTo(expectedGenreOpt.get());
    }

    @DisplayName("Проверка получения Автора по ФИО")
    @Test
    void shouldCorrectFindByDescription() {
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