package org.zav.service;

import org.springframework.stereotype.Service;
import org.zav.dao.BaseRepository;
import org.zav.model.Answer;
import org.zav.model.Question;

import java.util.List;

/**Сервис для вывода данных на экран*/
@Service
public class ScreenLayoutServiceImpl implements DataLayoutService {
    private final BaseRepository<Question> questionRepository;
    private final BaseRepository<Answer> answerRepository;

    public ScreenLayoutServiceImpl(BaseRepository<Question> questionRepository, BaseRepository<Answer> answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    private static final String ANSWER_INDENT = "   ";

    @Override
    public void showQuestionsWithAnswers() {
        List<Answer> answers = answerRepository.readAll();

        questionRepository.readAll().forEach(question-> {
            System.out.printf("%s. %s%n", question.getPositionNumber(), question.getQuestionDescription());
            answers.stream()
                    .filter(answer -> answer.getQuestionId().equals(question.getId()))
                    .sorted()
                    .map(item-> String.format("%s%s. %s",ANSWER_INDENT, item.getPositionNumber(), item.getAnswerDescription()))
                    .forEach(System.out::println);
        });

    }

    @Override
    public void showQuestionsOnly() {
        questionRepository.readAll().stream()
                .sorted()
                .forEach(item -> System.out.printf("%s. %s%n", item.getPositionNumber(), item.getQuestionDescription()));
    }
}
