package org.zav.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.zav.model.UserResult;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("Тестирование загрузки User из CSV в ресурсах")
public class UserResultCsvParserImplTest {
    Logger logger = LoggerFactory.getLogger(UserResultCsvParserImplTest.class);

    public static final String CSV_READ_BLANK_ERROR = "Can`t read CSV.";
    public static final String OBJECT_MATCH_ERROR = "The object read did not match the expected one.";
    final Resource testCsvResource = new AnnotationConfigApplicationContext().getResource("users_result_test.csv");

    @DisplayName("Проверка загрузки таблицы целиком")
    @Test
    void readAllFromCsvNotBlank() {
        BaseRepository<UserResult> testTarget = new UserResultCsvParserImpl(testCsvResource);

        List<UserResult> loadedData = null;
        try {
            loadedData = testTarget.readAll();
        } catch (AppDaoException e) {
            logger.error(BaseRepository.DATA_READING_FAILED);
            fail(BaseRepository.DATA_READING_FAILED);
        }

        assertFalse(loadedData.isEmpty(), CSV_READ_BLANK_ERROR);
    }

    @DisplayName("Проверка чтения эталонного обьекта User")
    @Test
    void readAllFromCsvIsValid() {
        BaseRepository<UserResult> testTarget = new UserResultCsvParserImpl(testCsvResource);

        List<UserResult> loadedData = null;
        try {
            loadedData = testTarget.readAll();
        } catch (AppDaoException e) {
            logger.error(BaseRepository.DATA_READING_FAILED);
            fail(BaseRepository.DATA_READING_FAILED);
        }

        UserResult actualData = loadedData.stream().sorted().findFirst().orElse(null);
        UserResult expectedData = new UserResult()
                .setId("e5fbcecc-eca8-41b4-8f75-5d592fc00e91")
                .setName("Igor")
                .setFamilyName("Riurikovich");

        assertEquals(actualData, expectedData, OBJECT_MATCH_ERROR);
    }
}
