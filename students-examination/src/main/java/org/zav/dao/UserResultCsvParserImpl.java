package org.zav.dao;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.zav.model.UserResult;
import org.zav.service.ResourceHolder;
import org.zav.utils.exceptions.AppDaoException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserResultCsvParserImpl implements BaseRepository<UserResult>{
/*    @Value("${sources.path.users}")
    private final Resource source;*/

    @Value("sources.path.users")
    private final String pathPropertyName;
    private final ResourceHolder resourceHolder;


    /**Получение всего набора данных*/
    @Override
    public List<UserResult> readAll() throws AppDaoException {
        return readAllBase(resourceHolder.getResource(pathPropertyName), UserResult.class);
    }

    /**Получение обьекта по ID*/
    @Nullable
    @Override
    public UserResult readById(String id) throws AppDaoException {
        return readByIdBase(id, resourceHolder.getResource(pathPropertyName), UserResult.class);
    }

    @Nullable
    @Override
    public String writeEntity(UserResult entity) throws AppDaoException{
        if(validateToWrite(entity) != null) return null;

        List<UserResult> currentUserResults = readAll();
        Optional<UserResult> oldUserStateOpt = currentUserResults.stream().filter(item-> item.isSameUser(entity)).findFirst();
        oldUserStateOpt.ifPresent(currentUserResults::remove);

        currentUserResults.add(entity);

        try(Writer writer = new OutputStreamWriter( new FileOutputStream(resourceHolder.getResource(pathPropertyName).getFile().getAbsoluteFile()))) {
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
