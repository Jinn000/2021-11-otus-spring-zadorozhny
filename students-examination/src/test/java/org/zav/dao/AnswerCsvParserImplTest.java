package org.zav.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.zav.Main;
import org.zav.model.Answer;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//TODO: как лучше получать ресурсы в тестовых классах?
//TODO: вижу дублирование кода с QuestionCsvParserImplTest. Как правильнее организовать тестирование?@ExtendWith(SpringExtension.class)
/*@ContextConfiguration(classes = Main.class)
@TestPropertySource("/application.properties")
@TestPropertySource("classpath:test.properties")*/
@DisplayName("Тестирование загрузки Answer из CSV в ресурсах")
@Slf4j
public class AnswerCsvParserImplTest {
    Logger logger = LoggerFactory.getLogger(AnswerCsvParserImplTest.class);

    public static final String CSV_READ_BLANK_ERROR = "Can`t read CSV.";
    public static final String OBJECT_MATCH_ERROR = "The object read did not match the expected one.";
    final Resource testCsvResource = new AnnotationConfigApplicationContext().getResource("answers_test.csv");

    @DisplayName("Проверка загрузки таблицы целиком")
    @Test
    void readAllFromCsvNotBlank() {
        AnswerCsvParserImpl testTarget = new AnswerCsvParserImpl(testCsvResource);

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
        AnswerCsvParserImpl testTarget = new AnswerCsvParserImpl(testCsvResource);

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

        assertEquals(actualData, expectedData, OBJECT_MATCH_ERROR);
    }
}
