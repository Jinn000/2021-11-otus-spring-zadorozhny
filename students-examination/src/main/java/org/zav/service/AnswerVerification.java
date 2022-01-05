package org.zav.service;

import lombok.NonNull;
import org.zav.utils.exceptions.AppDaoException;

public interface AnswerVerification {
    /**Сравнение ответа с правильным ответом.
     * @return Текст ошибки, с указанием правильного ответа*/
    @NonNull
    boolean verify(String questionId, String answerId) throws AppDaoException;
}
