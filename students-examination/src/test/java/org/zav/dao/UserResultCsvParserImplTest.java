package org.zav.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.zav.model.UserResult;
import org.zav.service.CsvResourceHolderImpl;
import org.zav.service.LocaleHolder;
import org.zav.service.ResourceHolder;
import org.zav.service.SingletonLocaleHolderImpl;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("Тестирование загрузки User из CSV в ресурсах")
public class UserResultCsvParserImplTest {
    Logger logger = LoggerFactory.getLogger(UserResultCsvParserImplTest.class);

    public static final String CSV_READ_BLANK_ERROR = "Can`t read CSV.";
    public static final String OBJECT_MATCH_ERROR = "The object read did not match the expected one.";
//    final Resource testCsvResource = new AnnotationConfigApplicationContext().getResource("users_result_test.csv");
    private final LocaleHolder localeHolder = SingletonLocaleHolderImpl.getInstance().setLocale(Locale.forLanguageTag("ru-RU"));
    private final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    private final ResourceHolder resourceHolder = new CsvResourceHolderImpl(localeHolder, messageSource);

    @DisplayName("Проверка загрузки таблицы целиком")
    @Test
    void readAllFromCsvNotBlank() {
        messageSource.setBasename("i18n/messages");
        BaseRepository<UserResult> testTarget = new UserResultCsvParserImpl("sources.path.users", resourceHolder);

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
        messageSource.setBasename("i18n/messages");
        BaseRepository<UserResult> testTarget = new UserResultCsvParserImpl("sources.path.users", resourceHolder);

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

        assertEquals(expectedData, actualData, OBJECT_MATCH_ERROR);
    }
}
