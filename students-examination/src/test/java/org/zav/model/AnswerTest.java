package org.zav.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Проверка класса Answer")
public class AnswerTest {

    public static final String OBJECT_COMPARISON_FAILED = "Object comparison failed.";
    public static final String EVEN_OBJECTS_COMPARISON_FAILED = "Even objects comparison failed.";

    @DisplayName("Сравнение двух не равных обьектов")
    @Test
    void compareToIsValid() {
        Answer left = new Answer();
        left.setId(0);
        left.setAnswerDescription("aa");
        left.setPositionNumber("b");
        left.setQuestionId(0);

        Answer right = new Answer();
        right.setId(0);
        right.setAnswerDescription("aa");
        right.setPositionNumber("c");
        right.setQuestionId(0);

        assertTrue(left.compareTo(right) < 0, OBJECT_COMPARISON_FAILED);
    }

    @DisplayName("Сравнение двух равных обьектов")
    @Test
    void evenObjectsCompareToIsValid() {
        Answer left = new Answer();
        left.setId(0);
        left.setAnswerDescription("aa");
        left.setPositionNumber("b");
        left.setQuestionId(0);

        Answer right = new Answer();
        right.setId(0);
        right.setAnswerDescription("aa");
        right.setPositionNumber("b");
        right.setQuestionId(0);

        assertEquals(left.compareTo(right), 0, EVEN_OBJECTS_COMPARISON_FAILED);
    }
}
