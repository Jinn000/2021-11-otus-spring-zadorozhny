package org.zav.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Проверка класса Question")
public class QuestionTest {

    public static final String OBJECT_COMPARISON_FAILED = "Object comparison failed.";
    public static final String EVEN_OBJECTS_COMPARISON_FAILED = "Even objects comparison failed.";

    @DisplayName("Сравнение двух не равных обьектов")
    @Test
    void compareToIsValid() {
        Question left = new Question();
        left.setId(0);
        left.setQuestionDescription("aa");
        left.setPositionNumber(1);
        left.setValidAnswerId(0);

        Question right = new Question();
        right.setId(5);
        right.setQuestionDescription("gg");
        right.setPositionNumber(4);
        right.setValidAnswerId(6);

        assertTrue(left.compareTo(right) < 0, OBJECT_COMPARISON_FAILED);
    }

    @DisplayName("Сравнение двух равных обьектов")
    @Test
    void evenObjectsCompareToIsValid() {
        Question left = new Question();
        left.setId(0);
        left.setQuestionDescription("aa");
        left.setPositionNumber(4);
        left.setValidAnswerId(0);

        Question right = new Question();
        right.setId(2);
        right.setQuestionDescription("gg");
        right.setPositionNumber(4);
        right.setValidAnswerId(6);

        assertEquals(left.compareTo(right), 0, EVEN_OBJECTS_COMPARISON_FAILED);
    }
}
