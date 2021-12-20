package org.zav.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.zav.model.Answer;
import org.zav.service.CsvResourceHolderImpl;
import org.zav.service.LocaleHolder;
import org.zav.service.ResourceHolder;
import org.zav.service.SingletonLocaleHolderImpl;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование загрузки Answer из CSV в ресурсах")
@Slf4j
public class AnswerCsvParserImplTest {
    Logger logger = LoggerFactory.getLogger(AnswerCsvParserImplTest.class);

    public static final String CSV_READ_BLANK_ERROR = "Can`t read CSV.";
    public static final String OBJECT_MATCH_ERROR = "The object read did not match the expected one.";
    private final LocaleHolder localeHolder = SingletonLocaleHolderImpl.getInstance().setLocale(Locale.forLanguageTag("ru-RU"));
    private final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    private final ResourceHolder resourceHolder = new CsvResourceHolderImpl(localeHolder, messageSource);

    @DisplayName("Проверка загрузки таблицы целиком")
    @Test
    void readAllFromCsvNotBlank() {
        messageSource.setBasename("i18n/messages");
        AnswerCsvParserImpl testTarget = new AnswerCsvParserImpl("sources.path.answers", resourceHolder);

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
        messageSource.setBasename("i18n/messages");
        AnswerCsvParserImpl testTarget = new AnswerCsvParserImpl("sources.path.answers", resourceHolder);

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
