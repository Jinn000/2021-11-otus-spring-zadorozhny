package org.zav.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.zav.dao.BaseRepository;
import org.zav.iu.LayoutService;
import org.zav.model.Answer;
import org.zav.model.Question;
import org.zav.model.UserResult;
import org.zav.testpropertysource.ClassUsingProperty;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**Сервис для обмена данными с пользователем*/
@Service
@RequiredArgsConstructor
public class ConsoleExaminationServiceImpl implements ExaminationService {
    private final BaseRepository<UserResult> userResultRepository;
    private final BaseRepository<Question> questionRepository;
    private final BaseRepository<Answer> answerRepository;
    private final LayoutService<String, String> layoutService;
    private final AnswerVerification answerVerification;
    private final ClassUsingProperty classUsingProperty;
    private final MessageSource messageSource;

    @Override
    public void run() {
        String userId = askUserData();
        if(userId == null) return;

        messageSource.getMessage("sources.path.answers", null, Locale.forLanguageTag("ru-RU"));
        runExamination(userId);
    }

    /**Вывод всех вопросов и вариантов ответов*/
    @Override
    public void showQuestionsWithAnswers() {
        List<Answer> answers;
        List<Question> questions;
        try {
            answers = answerRepository.readAll();
            questions = questionRepository.readAll();
        } catch (AppDaoException e) {
            layoutService.show(e.getMessage());
            return;
        }

        List<Answer> finalAnswers = answers;
        questions.forEach(question-> {
            layoutService.show(String.format("%s. %s", question.getPositionNumber(), question.getQuestionDescription()));
            List<String> messages = finalAnswers.stream()
                    .filter(answer -> answer.getQuestionId().equals(question.getId()))
                    .sorted()
                    .map(item-> String.format("%s%s. %s",ANSWER_INDENT, item.getPositionNumber(), item.getAnswerDescription()))
                    .collect(Collectors.toList());
            messages.forEach(layoutService::show);
        });

    }

    /**Вывод списка вопросов*/
    @Override
    public void showQuestionsOnly() {
        try {
            questionRepository.readAll().stream()
                    .sorted()
                    .forEach(item -> layoutService.show(String.format("%s. %s%n", item.getPositionNumber(), item.getQuestionDescription())));
        } catch (AppDaoException e) {
            layoutService.show(e.getMessage());
        }
    }

//--------------------------------------------------------------
    /**Запуск тестирования*/
    private void runExamination(String userId) {
        final List<Question> questions;
        try {
            questions = questionRepository.readAll();
        } catch (AppDaoException e) {
            layoutService.show(e.getMessage());
            return;
        }

        try {
            resetUserResult(userId);
        } catch (AppDaoException e) {
            layoutService.show(CAN_T_GET_USER_DATA_ERROR);
            return;
        }

        questions.forEach(q-> {
            UserResult userResult;
            String messageToUser;
            try {
                final String questionWithAnswers = getQuestionWithAnswers(q.getId());
                userResult = userResultRepository.readById(userId);
                final String answerId = layoutService.ask(questionWithAnswers);
                messageToUser = answerVerification.verify(q.getId(), answerId);
            } catch (AppDaoException e) {
                layoutService.show(e.getMessage());
                return;
            }

            if(userResult == null) {
                layoutService.show(CAN_T_GET_USER_DATA_ERROR);
                return;
            }

            layoutService.show(messageToUser);
            layoutService.show("\n");

            if(messageToUser.equals(AnswerVerification.GOOD)){
                int newValidCount = Integer.parseInt(userResult.getValidAnswerCount()) + 1;
                userResult.setValidAnswerCount(String.valueOf(newValidCount));

                try {
                    userResultRepository.writeEntity(userResult);
                } catch (AppDaoException e) {
                    layoutService.show(e.getMessage());
                }
            }
        });

        UserResult userResult;
        try {
            userResult = userResultRepository.readById(userId);
        } catch (AppDaoException e) {
            layoutService.show(e.getMessage());
            return;
        }

        if(userResult == null) {
            layoutService.show(BaseRepository.ID_MISSING);
            return;
        }

        String resultCountValid = userResult.getValidAnswerCount();
        layoutService.show(String.format("Your result is: %s valid answer of %d total.", resultCountValid, classUsingProperty.getQuestions().getCount()));
    }

    /**Запрос имени/фамилии*/
    private String askUserData() {

        String name;
        String familyName;

        do {
            name = layoutService.ask(ENTER_YOUR_NAME);
        } while (StringUtils.isAllBlank(name));

        do {
            familyName = layoutService.ask(ENTER_YOUR_FAMILY_NAME);
        } while (StringUtils.isAllBlank(familyName));

        String uuid = UUID.randomUUID().toString();

        String finalName = name;
        String finalFamilyName = familyName;
        Optional<UserResult> userOptional;
        try {
            userOptional = userResultRepository.readAll().stream()
                    .filter(u-> u.isSameUser(new UserResult().setName(finalName).setFamilyName(finalFamilyName)))
                    .findFirst();
        } catch (AppDaoException e) {
            layoutService.show(e.getMessage());
            return null;
        }

        if(userOptional.isPresent()){
            uuid = userOptional.get().getId();
        } else {
            try {
                userResultRepository.writeEntity(new UserResult().setId(uuid).setName(name).setFamilyName(familyName));
            } catch (AppDaoException e) {
                layoutService.show(I_CANNOT_SAVE_DATA_SORRY);
                return null;
            }
        }

        return uuid;
    }

    /**Получение форматированного блока текста
     * содержащего вопрос и варианты ответов.
     * */
    private String getQuestionWithAnswers(String questionId) throws AppDaoException {
        List<Answer> answers = answerRepository.readAll();
        Question question = questionRepository.readById(questionId);
        if (question == null) throw new AppDaoException(QUESTION_ID_MISSING);

        String answersByQuestion = answers.stream()
                .filter(answer -> answer.getQuestionId().equals(question.getId()))
                .sorted()
                .map(item-> String.format("%s%s. %s",ANSWER_INDENT, item.getPositionNumber(), item.getAnswerDescription()))
                .collect(Collectors.joining("\n"));

        return String.format("\n%s. %s \n%s", question.getPositionNumber(), question.getQuestionDescription(), answersByQuestion);
    }

    @Nullable
    private String resetUserResult(@NonNull String userId) throws AppDaoException {
        String status = null;
        Optional<UserResult> userResultOptional = Optional.ofNullable(userResultRepository.readById(userId));
        if(userResultOptional.isPresent()){
            UserResult userResult = userResultOptional.get();
            userResult.setValidAnswerCount("0");
            status = userResultRepository.writeEntity(userResult);
        }

        return status;
    }
}
