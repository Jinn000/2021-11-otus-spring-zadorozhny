package org.zav.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.zav.model.Question;
import org.zav.service.ResourceHolder;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuestionCsvParserImpl implements BaseRepository<Question> {

    @Value("sources.path.questions")
    private final String pathPropertyName;
    private final ResourceHolder resourceHolder;


    /**Получение всего набора данных*/
    @NonNull
    @Override
    public List<Question> readAll() throws AppDaoException {
        return readAllBase(resourceHolder.getResource(pathPropertyName), Question.class);
    }

    /**Получение обьекта по ID*/
    @Override
    public Question readById(@NonNull String id) throws AppDaoException {
        return readByIdBase(id, resourceHolder.getResource(pathPropertyName), Question.class);
    }

    /*TODO: TBD*/
    @Override
    public String writeEntity(Question question) {
        return null;
    }

    /*TODO: TBD*/
    @Override
    public boolean deleteById(String id) {
        return false;
    }
}
