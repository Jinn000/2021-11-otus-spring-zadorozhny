package org.zav.dao;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.zav.model.UserResult;
import org.zav.utils.exceptions.AppDaoException;

import java.io.*;
import java.util.List;
import java.util.Optional;

@PropertySource("classpath:application.properties")
@Service
public class UserResultCsvParserImpl implements BaseRepository<UserResult>{
    private final Resource source;

    public UserResultCsvParserImpl(@Value("${sources.path.users}") Resource source) {
        this.source = source;
    }

    /**Получение всего набора данных*/
    @Override
    public List<UserResult> readAll() {
        return readAllBase(source, UserResult.class);
    }

    /**Получение обьекта по ID*/
    @Nullable
    @Override
    public UserResult readById(String id) {
        return readByIdBase(id, source, UserResult.class);
    }

    @Nullable
    @Override
    public String writeEntity(UserResult entity) throws AppDaoException{
        if(validateToWrite(entity) != null) return null;

        List<UserResult> currentUserResults = readAll();
        Optional<UserResult> oldUserStateOpt = currentUserResults.stream().filter(item-> item.isSameUser(entity)).findFirst();
        oldUserStateOpt.ifPresent(currentUserResults::remove);

        currentUserResults.add(entity);

//        try(Writer writer = new FileWriter(source.getFile().toPath().toString())) {
        try(Writer writer = new OutputStreamWriter( new FileOutputStream(source.getFile().getAbsoluteFile()))) {
            StatefulBeanToCsv<UserResult> sbc = new StatefulBeanToCsvBuilder<UserResult>(writer)
                    .withSeparator('*')
                    .build();

            try {
                sbc.write(currentUserResults);
            } catch (CsvDataTypeMismatchException e) {
                throw new AppDaoException(DATA_TYPE_MISMATCH, e);
            } catch (CsvRequiredFieldEmptyException e) {
                throw new AppDaoException(REQUIRED_FIELD_EMPTY, e);
            }
        } catch (IOException e) {
            throw new AppDaoException(NO_STORAGE_ACCESS, e);
        }

        return entity.getId();
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }


    private String validateToWrite(UserResult entity){
        if(entity.getId() == null) return ID_MISSING;
        else return null;
    }
}
