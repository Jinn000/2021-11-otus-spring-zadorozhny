package org.zav.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.zav.dao.Entity;

@Data
public class Answer implements Entity, Comparable<Answer> {
    @CsvBindByName(column = "ID")
    private Integer id;

    @CsvBindByName(column = "ANSWER_DESCRIPTION")
    private String answerDescription;

    @CsvBindByName(column = "QUESTION_ID")
    private Integer questionId;

    @CsvBindByName(column = "POSITION_NUMBER")
    private String positionNumber;

    @Override
    public int compareTo(Answer o) {
        return this.getPositionNumber().compareToIgnoreCase(o.getPositionNumber());
    }
}
