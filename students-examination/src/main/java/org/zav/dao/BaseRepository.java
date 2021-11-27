package org.zav.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface BaseRepository<T extends Entity> {
    Logger logger = Logger.getGlobal();

    String READING_QUESTIONS_FAILED = "Reading questions failed ";


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

        String data = "";
        try {
            InputStreamReader isR = new InputStreamReader(source.getInputStream());
            byte[] bdata = FileCopyUtils.copyToByteArray(source.getInputStream());
            data = new String(bdata, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.log(Level.WARNING, READING_QUESTIONS_FAILED);
        }


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
