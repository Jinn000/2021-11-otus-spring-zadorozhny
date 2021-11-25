package org.zav.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zav.dao.BaseRepository;
import org.zav.model.Answer;
import org.zav.model.Question;

import java.util.stream.Collectors;

/**Сервис для вывода данных на экран*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScreenLayoutServiceImpl implements DataLayoutService {
    BaseRepository<Question> questionRepository;
    BaseRepository<Answer> answerRepository;

    @Override
    public void showQuestionsWithAnswers() {

    }

    @Override
    public void showQuestionsOnly() {
        questionRepository.readAll().stream()
                .map(Question::getQuestionDescription)
                .peek(System.out::println)
                .collect(Collectors.toList());
    }
}
