package org.zav.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.zav.model.Question;

import java.util.List;

//TODO: можно ли использовать ломбок RequiredConstructor , и както передавать в него Value?

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuestionCsvParserImpl implements BaseRepository<Question> {
    @Value("${sources.path.questions}")
    private final Resource source;


    /**Получение всего набора данных*/
    @NonNull
    @Override
    public List<Question> readAll() {
        return readAllBase(source, Question.class);
    }

    /**Получение обьекта по ID*/
    @Override
    public Question readById(@NonNull String id) {
        return readByIdBase(id, source, Question.class);
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
