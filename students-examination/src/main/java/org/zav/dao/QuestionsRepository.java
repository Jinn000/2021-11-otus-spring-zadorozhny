package org.zav.dao;

import org.zav.model.Question;

import java.util.List;

public interface QuestionsRepository {
    List<Question> readQuestions();
    Question read(Integer id);

    /**Запись вопроса (добавление или перезапись существующего)*/
    Integer write(Question question);
    /**Удаление по ID*/
    boolean delete(Integer id);
}
