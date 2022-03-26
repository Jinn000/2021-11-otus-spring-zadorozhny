package ru.zav.storedbooksinfo.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

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
        return String.format("%s %s %s", firstName, lastName, familyName);
    }
}
