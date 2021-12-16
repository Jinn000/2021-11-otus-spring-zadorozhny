package org.zav.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.zav.Main;
import org.zav.model.Answer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
//TODO: как лучше получать ресурсы в тестовых классах?
//TODO: вижу дуюлирование кода с QuestionCsvParserImplTest. Как правильнее организовать тестирование?
//TODO: classpath у тестов смотрит в туда же куда и у main? Не удалось подсунуть тесту файл ресурсов (lass path resource [${sources.path.answers}] cannot be opened because it does not exist)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Main.class)
@TestPropertySource("/application.properties")
@TestPropertySource("classpath:test.properties")
@DisplayName("Тестирование загрузки Answer из CSV в ресурсах")
public class AnswerCsvParserImplTest {

    public static final String CSV_READ_BLANK_ERROR = "Can`t read CSV.";
    public static final String OBJECT_MATCH_ERROR = "The object read did not match the expected one.";

    @Autowired
    BaseRepository<Answer> testTarget;
//    final Resource testCsvResource = new AnnotationConfigApplicationContext().getResource("answers_test.csv");

    @DisplayName("Проверка загрузки таблицы целиком")
    @Test
    void readAllFromCsvNotBlank() {
//        AnswerCsvParserImpl testTarget = new AnswerCsvParserImpl(testCsvResource);

        List<Answer> loadedData = testTarget.readAll();
        assertFalse(loadedData.isEmpty(), CSV_READ_BLANK_ERROR);
    }

    @DisplayName("Проверка чтения эталонного обьекта Answer")
    @Test
    void readAllFromCsvIsValid() {
//        AnswerCsvParserImpl testTarget = new AnswerCsvParserImpl(testCsvResource);

        List<Answer> loadedData = testTarget.readAll();

        Answer actualData = loadedData.stream().sorted().findFirst().orElse(null);
        Answer expectedData = new Answer();
        expectedData.setId("0");
        expectedData.setAnswerDescription("42");
        expectedData.setQuestionId("0");
        expectedData.setPositionNumber("a");

        assertEquals(actualData, expectedData, OBJECT_MATCH_ERROR);
    }
}
