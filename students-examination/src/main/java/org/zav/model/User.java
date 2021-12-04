package org.zav.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.zav.dao.Entity;

@Data
@Accessors(chain = true)
public class User implements Entity, Comparable<User> {
    @CsvBindByName(column = "USER_ID")
    private Integer id;
    @CsvBindByName(column = "USER_NAME")
    private String name;
    @CsvBindByName(column = "USER_FAMILY_NAME")
    private String familyName;

    @Override
    public int compareTo(User o) {
        return this.id.compareTo(o.id);
    }
}
