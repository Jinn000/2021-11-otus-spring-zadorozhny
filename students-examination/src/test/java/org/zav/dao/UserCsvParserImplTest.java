package org.zav.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.zav.Main;
import org.zav.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Тестирование загрузки User из CSV в ресурсах")
@ContextConfiguration(classes = Main.class)
public class UserCsvParserImplTest {

    public static final String CSV_READ_BLANK_ERROR = "Can`t read CSV.";
    public static final String OBJECT_MATCH_ERROR = "The object read did not match the expected one.";
    final Resource testCsvResource = new AnnotationConfigApplicationContext().getResource("users_test.csv");

    @DisplayName("Проверка загрузки таблицы целиком")
    @Test
    void readAllFromCsvNotBlank() {
        BaseRepository<User> testTarget = new UserCsvParserImpl(testCsvResource);

        List<User> loadedData = testTarget.readAll();
        assertFalse(loadedData.isEmpty(), CSV_READ_BLANK_ERROR);
    }

    @DisplayName("Проверка чтения эталонного обьекта User")
    @Test
    void readAllFromCsvIsValid() {
        BaseRepository<User> testTarget = new UserCsvParserImpl(testCsvResource);

        List<User> loadedData = testTarget.readAll();

        User actualData = loadedData.stream().sorted().findFirst().orElse(null);
        User expectedData = new User()
                .setId(0)
                .setName("Igor")
                .setFamilyName("Riurikovich");

        assertEquals(actualData, expectedData, OBJECT_MATCH_ERROR);
    }
}
