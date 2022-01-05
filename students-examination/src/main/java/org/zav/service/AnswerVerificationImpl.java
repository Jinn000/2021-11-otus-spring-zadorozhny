package org.zav.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zav.dao.BaseRepository;
import org.zav.model.Answer;
import org.zav.model.Question;
import org.zav.utils.exceptions.AppDaoException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AnswerVerificationImpl implements AnswerVerification{
    private final BaseRepository<Question> questionRepository;
    private final BaseRepository<Answer> answerRepository;

    /**Сравнение ответа с правильным ответом.
     * @return Текст ошибки, с указанием правильного ответа*/
    @NonNull
    @Override
    public boolean verify(String questionId, String answerPosition) throws AppDaoException {
        String validAnswerId = Objects.requireNonNull(questionRepository.readById(questionId)).getValidAnswerId();
        String validAnswerPosition = Objects.requireNonNull(answerRepository.readById(validAnswerId)).getPositionNumber();

        return validAnswerPosition.equals(answerPosition);
    }
}
