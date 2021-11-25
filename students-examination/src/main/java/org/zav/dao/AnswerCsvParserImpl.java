package org.zav.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.zav.model.Answer;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerCsvParserImpl implements BaseRepository<Answer> {
    private Resource source;

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
