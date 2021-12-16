package org.zav.service;


public interface ExaminationService {
    String ENTER_YOUR_NAME = "Enter your name: ";
    String ENTER_YOUR_FAMILY_NAME = "Enter your family name: ";
    String I_CANNOT_SAVE_DATA_SORRY = "I can`t save data. sorry.";
    String ANSWER_INDENT = "   ";
    String QUESTION_ID_MISSING = "Question ID missing.";
    String APPLICATION_ERROR_SORRY = "Application error. Sorry.";
    String CAN_T_GET_USER_DATA_ERROR = "Error! Can't get user data.";

    /**Запуск тестирования*/
    void run();

    /**Вывод всех вопросов и вариантов ответов*/
    void showQuestionsWithAnswers();

    /**Вывод списка вопросов*/
    void showQuestionsOnly();
}
