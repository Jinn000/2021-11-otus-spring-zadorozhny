package org.zav.service;

@SuppressWarnings("unused")
public interface ExaminationService {
    String ANSWER_INDENT = "   ";
    String APPLICATION_ERROR_SORRY = "Application error. Sorry.";

    /**Запуск тестирования*/
    void run();

    /**Вывод всех вопросов и вариантов ответов*/
    void showQuestionsWithAnswers();

    /**Вывод списка вопросов*/
    void showQuestionsOnly();
}
