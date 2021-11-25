package org.zav.model;

import lombok.Data;

@Data
public class Answer {
    Integer id;
    String answerDescription;
    Integer questionId;
}
