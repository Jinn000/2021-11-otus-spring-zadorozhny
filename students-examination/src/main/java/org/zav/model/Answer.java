package org.zav.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.zav.dao.Entity;

@Data
public class Answer implements Entity {
    @CsvBindByName(column = "ID")
    Integer id;

    @CsvBindByName(column = "ANSWER_DESCRIPTION")
    String answerDescription;

    @CsvBindByName(column = "QUESTION_ID")
    Integer questionId;
}
