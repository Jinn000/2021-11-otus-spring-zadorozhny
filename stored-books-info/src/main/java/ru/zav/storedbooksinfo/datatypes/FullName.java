package ru.zav.storedbooksinfo.datatypes;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Builder
@Data
public class FullName {
    private String firstName;
    private String lastName;
    private String familyName;

    public void setFirstName(String firstName) {
        this.firstName = StringUtils.trim(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = StringUtils.trim(lastName);
    }

    public void setFamilyName(String familyName) {
        this.familyName = StringUtils.trim(familyName);
    }

    @Override
    public String toString(){
        return String.format("%s %s %s", this.firstName, this.lastName, this.familyName);
    }
}
