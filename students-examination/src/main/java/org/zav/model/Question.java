package org.zav.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class Question {
    @CsvBindByName(column = "ID")
    Integer id;

    @CsvBindByName(column = "QUESTION_DESCRIPTION")
    String questionDescription;

    @CsvBindByName(column = "VALID_ANSWER_ID")
    Integer validAnswerId;
}
