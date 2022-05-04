package ru.zav.storedbooksinfo.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.zav.storedbooksinfo.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Проверка DAO работы с Жанрами:")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

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

    @DisplayName("Проверка получения Жанра по наименованию.")
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