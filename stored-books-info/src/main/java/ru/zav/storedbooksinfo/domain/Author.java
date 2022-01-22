package ru.zav.storedbooksinfo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Author {
    private String id;
    private String firstName;
    private String lastName;
    private String familyName;
}
