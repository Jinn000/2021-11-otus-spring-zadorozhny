package org.zav.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.zav.model.Answer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
//TODO: как лучше получать ресурсы в тестовых классах?
//TODO: нормально ли создавать тут свой ClassPathXmlApplicationContext?
//TODO: вижу дуюлирование кода с QuestionCsvParserImplTest. Как правильнее организовать тестирование?

@DisplayName("Тестирование загрузки Answer из CSV в ресурсах")
public class AnswerCsvParserImplTest {

    public static final String CSV_READ_BLANK_ERROR = "Can`t read CSV.";
    public static final String OBJECT_MATCH_ERROR = "The object read did not match the expected one.";
    final Resource testCsvResource = new ClassPathXmlApplicationContext("/spring-context.xml").getResource("/answers_test.csv");

    @DisplayName("Проверка загрузки таблицы целиком")
    @Test
    void readAllFromCsvNotBlank() {
        AnswerCsvParserImpl testTarget = new AnswerCsvParserImpl(testCsvResource);

        List<Answer> loadedData = testTarget.readAll();
        assertFalse(loadedData.isEmpty(), CSV_READ_BLANK_ERROR);
    }

    @DisplayName("Проверка чтения эталонного обьекта Answer")
    @Test
    void readAllFromCsvIsValid() {
        AnswerCsvParserImpl testTarget = new AnswerCsvParserImpl(testCsvResource);

        List<Answer> loadedData = testTarget.readAll();

        Answer actualData = loadedData.stream().sorted().findFirst().orElse(null);
        Answer expectedData = new Answer();
        expectedData.setId(0);
        expectedData.setAnswerDescription("42");
        expectedData.setQuestionId(0);
        expectedData.setPositionNumber("a");

        assertEquals(actualData, expectedData, OBJECT_MATCH_ERROR);
    }
}
