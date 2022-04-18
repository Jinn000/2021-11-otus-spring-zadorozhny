package ru.zav.storedbooksinfo.datatypes;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import ru.zav.storedbooksinfo.utils.AppServiceException;

@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public static FullName valueOf(String fullName) throws AppServiceException {
        try {
            final String[] names = StringUtils.split(fullName, " ");

            return FullName.builder()
                    .firstName(names[0])
                    .lastName(names[1])
                    .familyName(names[2])
                    .build();
        } catch (Exception e) {
            throw new AppServiceException("Не удалось строку с ФИО преобразовать в структуру FullName", e);
        }
    }
}
