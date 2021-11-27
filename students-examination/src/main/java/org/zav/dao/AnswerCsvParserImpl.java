package org.zav.dao;

import lombok.*;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.zav.model.Answer;

import java.util.List;

@RequiredArgsConstructor
public class AnswerCsvParserImpl implements BaseRepository<Answer> {
    private final Resource source;

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
