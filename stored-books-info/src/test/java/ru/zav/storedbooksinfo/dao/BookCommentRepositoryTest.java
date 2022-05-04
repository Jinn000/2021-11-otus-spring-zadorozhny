package ru.zav.storedbooksinfo.dao;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.zav.storedbooksinfo.domain.BookComment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверка DAO работы с Комментариями:")
@Data
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class BookCommentRepositoryTest {
    @Autowired
    private BookCommentRepository bookCommentRepository;
    @Autowired
    private BookRepository bookRepository;

    private BookComment expectedBookComment;

    @DisplayName("Проверка получения Комментариев по содержащемуся слову")
    @Test
    void shouldCorrectFindByWord(){
        final String word = "осуждаю";
        final List<BookComment> byCommentContaining = bookCommentRepository.findByCommentContaining(word);
        assertThat(byCommentContaining.size()).isEqualTo(1);
        assertThat(byCommentContaining.get(0).getComment()).isEqualTo("Не читал, но осуждаю.");
    }

    @DisplayName("Проверка получения Комментариев по имени комментатора")
    @Test
    void shouldCorrectFindByName(){
        final String name = "Сергей";
        final List<BookComment> byName = bookCommentRepository.findByNameContaining(name);
        assertThat(byName.size()).isEqualTo(1);
        assertThat(byName.get(0).getComment()).isEqualTo("Не читал, но осуждаю.");
    }
}