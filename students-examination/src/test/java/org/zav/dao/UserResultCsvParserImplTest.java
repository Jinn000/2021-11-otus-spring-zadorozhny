package org.zav.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.zav.model.UserResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Тестирование загрузки User из CSV в ресурсах")
public class UserResultCsvParserImplTest {

    public static final String CSV_READ_BLANK_ERROR = "Can`t read CSV.";
    public static final String OBJECT_MATCH_ERROR = "The object read did not match the expected one.";
    final Resource testCsvResource = new AnnotationConfigApplicationContext().getResource("users_result_test.csv");

    @DisplayName("Проверка загрузки таблицы целиком")
    @Test
    void readAllFromCsvNotBlank() {
        BaseRepository<UserResult> testTarget = new UserResultCsvParserImpl(testCsvResource);

        List<UserResult> loadedData = testTarget.readAll();
        assertFalse(loadedData.isEmpty(), CSV_READ_BLANK_ERROR);
    }

    @DisplayName("Проверка чтения эталонного обьекта User")
    @Test
    void readAllFromCsvIsValid() {
        BaseRepository<UserResult> testTarget = new UserResultCsvParserImpl(testCsvResource);

        List<UserResult> loadedData = testTarget.readAll();

        UserResult actualData = loadedData.stream().sorted().findFirst().orElse(null);
        UserResult expectedData = new UserResult()
                .setId("e5fbcecc-eca8-41b4-8f75-5d592fc00e91")
                .setName("Igor")
                .setFamilyName("Riurikovich");

        assertEquals(actualData, expectedData, OBJECT_MATCH_ERROR);
    }
}
