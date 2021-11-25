package org.zav.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.zav.model.Question;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface BaseRepository<T extends Entity> {
    static Logger logger = Logger.getGlobal();

    static final String READING_QUESTIONS_FAILED = "Reading questions failed ";


    /**Получение всего набора данных*/
    @NonNull
    List<T> readAll();

    /**Получение entity по ID*/
    @Nullable
    T readById(@NonNull Integer id);

    /**Запись entity (добавление или перезапись существующего)*/
    Integer writeEntity(T entity);
    /**Удаление по ID*/
    boolean deleteById(Integer id);

    @NonNull
    default List<T> readAllBase(Resource source, Class<T> type) {
        List<T> result = new ArrayList<>();

        try {
            File csv = source.getFile();
            Reader targetReader = new FileReader(csv);

            result = new CsvToBeanBuilder<T>(targetReader)
                    .withType(type)
                    .build()
                    .parse();
            targetReader.close();

        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, READING_QUESTIONS_FAILED);
        }

        return result;
    }

    default T readByIdBase(@NonNull Integer id, @NonNull Resource source,@NonNull Class<T> type) {
        return readAllBase(source, type).stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst()
                .orElse(null);
    }
}
