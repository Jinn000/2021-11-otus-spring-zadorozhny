package org.zav.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.zav.dao.Entity;

@Data
public class Question implements Entity {
    @CsvBindByName(column = "ID")
    Integer id;

    @CsvBindByName(column = "QUESTION_DESCRIPTION")
    String questionDescription;

    @CsvBindByName(column = "VALID_ANSWER_ID")
    Integer validAnswerId;
}
