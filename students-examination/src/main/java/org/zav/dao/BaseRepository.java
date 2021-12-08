package org.zav.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.zav.utils.exceptions.AppDaoException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public interface BaseRepository<T extends Entity> {
    String NO_STORAGE_ACCESS = "No storage access";
    String DATA_TYPE_MISMATCH = "Data type mismatch";
    String REQUIRED_FIELD_EMPTY = "Required field empty";
    String ID_MISSING = "ID MISSING";
    String READING_QUESTIONS_FAILED = "Reading questions failed";
    String DATA_READING_FAILED = "Data reading failed";


    Logger logger = Logger.getGlobal();


    /**Получение всего набора данных*/
    @NonNull
    List<T> readAll() throws AppDaoException;

    /**Получение entity по ID*/
    @Nullable
    T readById(@NonNull String id) throws AppDaoException;

    /**Запись entity (добавление или перезапись существующего)*/
    String writeEntity(T entity) throws AppDaoException;
    /**Удаление по ID*/
    boolean deleteById(String id) throws AppDaoException;

    @NonNull
    default List<T> readAllBase(Resource source, Class<T> type) throws AppDaoException {
        List<T> result = new ArrayList<>();
        try {
            InputStreamReader targetReader = new InputStreamReader(source.getInputStream());
            result = new CsvToBeanBuilder<T>(targetReader)
                    .withSeparator('*')
                    .withType(type)
                    .build()
                    .parse();
            targetReader.close();

        } catch (IOException e) {
            e.printStackTrace();
            logger.warning(DATA_READING_FAILED);
            throw new AppDaoException(DATA_READING_FAILED, e);
        }

        return result;
    }

    @Nullable
    default T readByIdBase(@NonNull String id, @NonNull Resource source,@NonNull Class<T> type) throws AppDaoException{
        return readAllBase(source, type).stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst()
                .orElse(null);
    }
}
