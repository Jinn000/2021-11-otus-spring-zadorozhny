package org.zav.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.zav.model.Question;
import org.zav.service.CsvResourceHolderImpl;
import org.zav.service.LocaleHolder;
import org.zav.service.ResourceHolder;
import org.zav.service.SingletonLocaleHolderImpl;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("Тестирование загрузки Question из CSV в ресурсах")
public class QuestionCsvParserImplTest {
    Logger logger = LoggerFactory.getLogger(QuestionCsvParserImplTest.class);

    public static final String CSV_READ_BLANK_ERROR = "Can`t read CSV.";
    public static final String OBJECT_MATCH_ERROR = "The object read did not match the expected one.";
//    private final Resource testCsvResource = new AnnotationConfigApplicationContext().getResource("questions_test.csv");
    private final LocaleHolder localeHolder = SingletonLocaleHolderImpl.getInstance().setLocale(Locale.forLanguageTag("ru-RU"));
    private final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    private final ResourceHolder resourceHolder = new CsvResourceHolderImpl(localeHolder, messageSource);

    @DisplayName("Проверка загрузки таблицы целиком")
    @Test
    void readAllFromCsvNotBlank() {
        messageSource.setBasename("i18n/messages");
        QuestionCsvParserImpl testTarget = new QuestionCsvParserImpl("sources.path.questions", resourceHolder);
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
        messageSource.setBasename("i18n/messages");
        QuestionCsvParserImpl testTarget = new QuestionCsvParserImpl("sources.path.questions", resourceHolder);

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
        expectedData.setQuestionDescription(messageSource.getMessage("expected.test.questions", null, localeHolder.getLocale()));
        expectedData.setValidAnswerId("0");

        assertEquals(expectedData, actualData, OBJECT_MATCH_ERROR);
    }
}
