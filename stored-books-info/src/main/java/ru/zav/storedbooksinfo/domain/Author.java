package ru.zav.storedbooksinfo.domain;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "AUTHOR")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "FIRST_NAME", length = 64)
    private String firstName;

    @Column(name = "LAST_NAME", length = 64)
    private String lastName;

    @Column(name = "FAMILY_NAME", length = 64)
    private String familyName;

    @Override
    public String toString() {
        List<String> fullNameList = List.of(getFirstName(), getLastName(), getFamilyName()).stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        return String.format("%s", String.join(" ", fullNameList));
    }
}
