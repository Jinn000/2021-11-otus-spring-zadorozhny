package org.zav.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zav.dao.BaseRepository;
import org.zav.iu.LayoutService;
import org.zav.model.Answer;
import org.zav.model.Question;
import org.zav.model.UserResult;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**Сервис для обмена данными с пользователем*/
@Service
public class ConsoleExaminationServiceImpl implements ExaminationService {
    private final BaseRepository<UserResult> userResultRepository;
    private final BaseRepository<Question> questionRepository;
    private final BaseRepository<Answer> answerRepository;
    private final LayoutService<String, String> layoutService;
    private final AnswerVerification answerVerification;

    public ConsoleExaminationServiceImpl(BaseRepository<UserResult> userResultRepository
            , BaseRepository<Question> questionRepository
            , BaseRepository<Answer> answerRepository
            , LayoutService<String, String> layoutService
            , AnswerVerification answerVerification
            , @Value("${questions.count}") String totalQuestionsCount) {
        this.userResultRepository = userResultRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.layoutService = layoutService;
        this.answerVerification = answerVerification;
        this.totalQuestionsCount = totalQuestionsCount;
    }


    private final String totalQuestionsCount;


    @Override
    public void showQuestionsWithAnswers() {
        List<Answer> answers = answerRepository.readAll();

        questionRepository.readAll().forEach(question-> {
            layoutService.show(String.format("%s. %s", question.getPositionNumber(), question.getQuestionDescription()));
            List<String> messages = answers.stream()
                    .filter(answer -> answer.getQuestionId().equals(question.getId()))
                    .sorted()
                    .map(item-> String.format("%s%s. %s",ANSWER_INDENT, item.getPositionNumber(), item.getAnswerDescription()))
                    .collect(Collectors.toList());
            messages.forEach(layoutService::show);
        });

    }

    @Override
    public void showQuestionsOnly() {
        questionRepository.readAll().stream()
                .sorted()
                .forEach(item -> layoutService.show(String.format("%s. %s%n", item.getPositionNumber(), item.getQuestionDescription())));
    }

    @Override
    public void runExamination(String userId) {
        List<Question> questions = questionRepository.readAll();
        questions.forEach(q-> {
            String questionWithAnswers;
            try {
                questionWithAnswers = getQuestionWithAnswers(q.getId());
            } catch (AppDaoException e) {
                e.printStackTrace();
                layoutService.show(APPLICATION_ERROR_SORRY);
                return;
            }

            String answerId = layoutService.ask(questionWithAnswers);
            String messageToUser = answerVerification.verify(q.getId(), answerId);

            layoutService.show(messageToUser);
            layoutService.show("\n");

            UserResult userResult = userResultRepository.readById(userId);

            if(userResult == null) {
                layoutService.show(CAN_T_GET_USER_DATA_ERROR);
                return;
            }

            if(messageToUser.equals(AnswerVerification.GOOD)){
                int newValidCount = Integer.parseInt(userResult.getValidAnswerCount()) + 1;
                userResult.setValidAnswerCount(String.valueOf(newValidCount));

                try {
                    userResultRepository.writeEntity(userResult);
                } catch (AppDaoException e) {
                    e.printStackTrace();
                    layoutService.show(BaseRepository.NO_STORAGE_ACCESS);
                }
            }
        });

        UserResult userResult = userResultRepository.readById(userId);
        if(userResult == null) {
            layoutService.show(BaseRepository.ID_MISSING);
            return;
        }

        String resultCountValid = userResult.getValidAnswerCount();
        layoutService.show(String.format("Your result is: %s valid answer of %s total.", resultCountValid, totalQuestionsCount));
    }

    @Override
    public String askUserData() {

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
        var userOptional = userResultRepository.readAll().stream()
                .filter(u-> u.isSameUser(new UserResult().setName(finalName).setFamilyName(finalFamilyName)))
                .findFirst();

        if(userOptional.isPresent()){
            uuid = userOptional.get().getId();
        } else {
            try {
                userResultRepository.writeEntity(new UserResult().setId(uuid).setName(name).setFamilyName(familyName));
            } catch (AppDaoException e) {
                e.printStackTrace();
                layoutService.show(I_CANNOT_SAVE_DATA_SORRY);
            }
        }

        return uuid;
    }
    //----------------------------------------

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
}
