package org.zav.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.zav.model.Question;

import java.util.List;

//TODO: можно ли использовать ломбок RequiredConstructor , и както передавать в него Value?

@Service
public class QuestionCsvParserImpl implements BaseRepository<Question> {
    private final Resource source;

    public QuestionCsvParserImpl(@Value("${sources.path.questions}") Resource source) {
        this.source = source;
    }

    /**Получение всего набора данных*/
    @NonNull
    @Override
    public List<Question> readAll() {
        return readAllBase(source, Question.class);
    }

    /**Получение обьекта по ID*/
    @Override
    public Question readById(@NonNull Integer id) {
        return readByIdBase(id, source, Question.class);
    }

    /*TODO: TBD*/
    @Override
    public Integer writeEntity(Question question) {
        return null;
    }

    /*TODO: TBD*/
    @Override
    public boolean deleteById(Integer id) {
        return false;
    }
}
