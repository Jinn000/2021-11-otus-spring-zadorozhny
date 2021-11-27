package org.zav.dao;

import lombok.*;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.zav.model.Question;

import java.util.List;

@RequiredArgsConstructor
public class QuestionCsvParserImpl implements BaseRepository<Question> {
    private final Resource source;

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
