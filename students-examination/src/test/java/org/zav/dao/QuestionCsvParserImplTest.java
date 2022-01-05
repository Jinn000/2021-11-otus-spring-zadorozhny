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
import org.zav.model.Question;
import org.zav.service.LocalizedMessageSourceImpl;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("Тестирование загрузки Question из CSV в ресурсах")
@ContextConfiguration(classes = Main.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QuestionCsvParserImplTest {
    Logger logger = LoggerFactory.getLogger(QuestionCsvParserImplTest.class);

    public static final String CSV_READ_BLANK_ERROR = "Can`t read CSV.";
    public static final String OBJECT_MATCH_ERROR = "The object read did not match the expected one.";

    private final LocalizedMessageSourceImpl messageSource;
    private final BaseRepository<Question> testTarget;

    @Autowired
    public QuestionCsvParserImplTest(LocalizedMessageSourceImpl messageSource, BaseRepository<Question> testTarget) {
        this.messageSource = messageSource;
        this.testTarget = testTarget;
    }

    @DisplayName("Проверка загрузки таблицы целиком")
    @Test
    void readAllFromCsvNotBlank() {
        List<Question> loadedData = null;
        try {
            loadedData = testTarget.readAll();
        } catch (AppDaoException e) {
            logger.error(BaseRepository.DATA_READING_FAILED);
            fail(BaseRepository.DATA_READING_FAILED);
        }
        assertFalse(loadedData.isEmpty(), CSV_READ_BLANK_ERROR);
    }

    @DisplayName("Проверка чтения эталонного обьекта Question")
    @Test
    void readAllFromCsvIsValid() {
        List<Question> loadedData = null;
        try {
            loadedData = testTarget.readAll();
        } catch (AppDaoException e) {
            logger.error(BaseRepository.DATA_READING_FAILED);
            fail(BaseRepository.DATA_READING_FAILED);
        }

        Question actualData = loadedData.stream().sorted().findFirst().orElse(null);
        Question expectedData = new Question();
        expectedData.setId("0");
        expectedData.setPositionNumber(1);
        expectedData.setQuestionDescription(messageSource.getLocalizedMessage("expected.test.questions", null));
        expectedData.setValidAnswerId("0");

        assertEquals(expectedData, actualData, OBJECT_MATCH_ERROR);
    }
}
