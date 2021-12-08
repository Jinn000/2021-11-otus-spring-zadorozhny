package org.zav.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.zav.dao.Entity;

import java.util.Objects;

@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class UserResult implements Entity {
    @CsvBindByName(column = "USER_ID")
    private String id;
    @CsvBindByName(column = "USER_NAME")
    private String name;
    @CsvBindByName(column = "USER_FAMILY_NAME")
    private String familyName;
    @CsvBindByName(column = "VALID_ANSWER_COUNT")
    private String validAnswerCount = "0";

    public boolean isSameUser(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final UserResult other = (UserResult) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.familyName, other.familyName);
    }

}