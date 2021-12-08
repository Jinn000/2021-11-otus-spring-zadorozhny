package org.zav.service;

import lombok.NonNull;
import org.zav.utils.exceptions.AppDaoException;

public interface AnswerVerification {
    String YOU_ARE_MISTAKEN_THE_CORRECT_ANSWER_IS = "You are mistaken. The correct answer is";
    String GOOD = "Good!";

    /**Сравнение ответа с правильным ответом.
     * @return Текст ошибки, с указанием правильного ответа*/
    @NonNull
    String verify(String questionId, String answerId) throws AppDaoException;
}
