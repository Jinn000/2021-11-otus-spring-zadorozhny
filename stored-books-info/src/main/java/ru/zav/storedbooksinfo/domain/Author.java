package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Document(collection = "author")
public class Author {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String familyName;

    @Override
    public String toString() {
        List<String> fullNameList = List.of(getFirstName(), getLastName(), getFamilyName()).stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        return String.format("%s", String.join(" ", fullNameList));
    }
}
