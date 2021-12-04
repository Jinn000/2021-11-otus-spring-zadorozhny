package org.zav.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.zav.model.Answer;
import org.zav.utils.exceptions.AppDaoException;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class AnswerCsvParserImpl implements BaseRepository<Answer> {
    private final Resource source;

    public AnswerCsvParserImpl(@Value("${sources.path.answers}") Resource source) {
        this.source = source;
    }

    /**Получение всего набора данных*/
    @NonNull
    @Override
    public List<Answer> readAll() {
        return readAllBase(source, Answer.class);
    }

    /**Получение обьекта по ID*/
    @Override
    public Answer readById(@NonNull Integer id) {
        return readByIdBase(id, source, Answer.class);
    }

    /*TODO: TBD*/
    @Override
    public Integer writeEntity(Answer answer) {
        return null;
    }

    /*TODO: TBD*/
    @Override
    public boolean deleteById(Integer id) {
        return false;
    }

}
