package org.zav.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.zav.dao.Entity;

@Data
public class Question implements Entity, Comparable<Question> {
    @CsvBindByName(column = "ID")
    private String id;

    @CsvBindByName(column = "POSITION_NUMBER")
    private Integer positionNumber;

    @CsvBindByName(column = "QUESTION_DESCRIPTION")
    private String questionDescription;

    @CsvBindByName(column = "VALID_ANSWER_ID")
    private String validAnswerId;

    @Override
    public int compareTo(Question o) {
        return this.getPositionNumber().compareTo(o.getPositionNumber());
    }
}
