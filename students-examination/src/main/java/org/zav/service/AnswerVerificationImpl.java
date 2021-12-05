package org.zav.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zav.dao.BaseRepository;
import org.zav.model.Answer;
import org.zav.model.Question;

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
    public String verify(String questionId, String answerPosition) {
        String validAnswerId = Objects.requireNonNull(questionRepository.readById(questionId)).getValidAnswerId();
        String validAnswerPosition = Objects.requireNonNull(answerRepository.readById(validAnswerId)).getPositionNumber();

        return validAnswerPosition.equals(answerPosition) ? GOOD : String.format("%s: %s", YOU_ARE_MISTAKEN_THE_CORRECT_ANSWER_IS, Objects.requireNonNull(answerRepository.readById(validAnswerId)).getAnswerDescription());
    }
}
