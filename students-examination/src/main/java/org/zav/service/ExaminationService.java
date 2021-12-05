package org.zav.service;


@SuppressWarnings("unused")
public interface ExaminationService {
    String ENTER_YOUR_NAME = "Enter your name: ";
    String ENTER_YOUR_FAMILY_NAME = "Enter your family name: ";
    String I_CANNOT_SAVE_DATA_SORRY = "I cannot save data. sorry.";
    String ANSWER_INDENT = "   ";
    String QUESTION_ID_MISSING = "Question ID missing.";
    String APPLICATION_ERROR_SORRY = "Application error. Sorry.";

    /**Запрос имени/фамилии*/
    String askUserData();

    /**Вывод всех вопросов и вараантов ответов*/
    void showQuestionsWithAnswers();

    /**Вывод списка вопросов*/
    void showQuestionsOnly();

    /**Запуск тестирования*/
    void runExamination(String userId);
}
