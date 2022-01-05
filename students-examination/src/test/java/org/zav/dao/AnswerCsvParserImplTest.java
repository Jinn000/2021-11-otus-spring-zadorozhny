package org.zav.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zav.Main;
import org.zav.model.Answer;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("Тестирование загрузки Answer из CSV в ресурсах")
@ContextConfiguration(classes = Main.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AnswerCsvParserImplTest {
    Logger logger = LoggerFactory.getLogger(AnswerCsvParserImplTest.class);

    public static final String CSV_READ_BLANK_ERROR = "Can`t read CSV.";
    public static final String OBJECT_MATCH_ERROR = "The object read did not match the expected one.";

    private final BaseRepository<Answer> testTarget;

    @Autowired
    public AnswerCsvParserImplTest(BaseRepository<Answer> testTarget) {
        this.testTarget = testTarget;
    }

    @DisplayName("Проверка загрузки таблицы целиком")
    @Test
    void readAllFromCsvNotBlank() {
        List<Answer> loadedData = null;
        try {
            loadedData = testTarget.readAll();
        } catch (AppDaoException e) {
            logger.error(BaseRepository.DATA_READING_FAILED);
            fail(BaseRepository.DATA_READING_FAILED);
        }
        assertFalse(loadedData.isEmpty(), CSV_READ_BLANK_ERROR);
    }

    @DisplayName("Проверка чтения эталонного обьекта Answer")
    @Test
    void readAllFromCsvIsValid() {
        List<Answer> loadedData = null;
        try {
            loadedData = testTarget.readAll();
        } catch (AppDaoException e) {
            logger.error(BaseRepository.DATA_READING_FAILED);
            fail(BaseRepository.DATA_READING_FAILED);
        }

        Answer actualData = loadedData.stream().sorted().findFirst().orElse(null);
        Answer expectedData = new Answer();
        expectedData.setId("0");
        expectedData.setAnswerDescription("42");
        expectedData.setQuestionId("0");
        expectedData.setPositionNumber("a");

        assertEquals(expectedData, actualData, OBJECT_MATCH_ERROR);
    }
}
