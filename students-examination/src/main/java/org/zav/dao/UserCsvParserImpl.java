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
import org.zav.model.User;
import org.zav.utils.exceptions.AppDaoException;

import java.io.*;
import java.util.List;
import java.util.Optional;

@PropertySource("classpath:application.properties")
@Service
public class UserCsvParserImpl implements BaseRepository<User>{
    public static final String NO_STORAGE_ACCESS = "No storage access";
    public static final String DATA_TYPE_MISMATCH = "Data type mismatch";
    public static final String REQUIRED_FIELD_EMPTY = "Required field empty";
    public static final String FAILED_TO_CLOSE_STORAGE_CONNECTION = "Failed to close storage connection";
    public static final String ERROR_ID_EXISTS = "Error. ID exists ";
    public static final String ID_MISSING = "ID MISSING";


    private final Resource source;

    public UserCsvParserImpl(@Value("${sources.path.users}") Resource source) {
        this.source = source;
    }

    /**Получение всего набора данных*/
    @Override
    public List<User> readAll() {
        return readAllBase(source, User.class);
    }

    /**Получение обьекта по ID*/
    @Nullable
    @Override
    public User readById(Integer id) {
        return readByIdBase(id, source, User.class);
    }

    @Nullable
    @Override
    public Integer writeEntity(User entity) throws AppDaoException{
        if(validateToWrite(entity) != null) return null;

        List<User> currentUsers = readAll();
        Optional<User> oldUserStateOpt = currentUsers.stream().filter(item-> item.compareTo(entity) == 0).findFirst();
        oldUserStateOpt.ifPresent(currentUsers::remove);

        currentUsers.add(entity);

        try(Writer writer = new FileWriter(source.getURI().getPath())) {
            StatefulBeanToCsv<User> sbc = new StatefulBeanToCsvBuilder<User>(writer)
                    .withSeparator('*')
                    .build();

            try {
                sbc.write(currentUsers);
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
    public boolean deleteById(Integer id) {
        return false;
    }


    private String validateToWrite(User entity){
        if(entity.getId() == null) return ID_MISSING;
        else return null;
    }
}
