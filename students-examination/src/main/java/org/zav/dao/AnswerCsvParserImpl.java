package org.zav.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.zav.model.Answer;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnswerCsvParserImpl implements BaseRepository<Answer> {
    @Value("${sources.path.answers}")
    private final Resource source;


    /**Получение всего набора данных*/
    @NonNull
    @Override
    public List<Answer> readAll() throws AppDaoException{
        return readAllBase(source, Answer.class);
    }

    /**Получение обьекта по ID*/
    @Override
    public Answer readById(@NonNull String id) throws AppDaoException {
        return readByIdBase(id, source, Answer.class);
    }

    /*TODO: TBD*/
    @Override
    public String writeEntity(Answer answer) {
        return null;
    }

    /*TODO: TBD*/
    @Override
    public boolean deleteById(String id) {
        return false;
    }

}
