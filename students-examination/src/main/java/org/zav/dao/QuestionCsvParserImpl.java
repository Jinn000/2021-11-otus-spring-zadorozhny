package org.zav.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.zav.model.Question;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCsvParserImpl implements BaseRepository<Question> {
    private Resource source;

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
