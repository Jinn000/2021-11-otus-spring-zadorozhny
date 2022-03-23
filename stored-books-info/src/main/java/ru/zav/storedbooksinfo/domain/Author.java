package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
public class Author {
    private String id;
    private String firstName;
    private String lastName;
    private String familyName;

    @Override
    public String toString() {
        return String.format("%s %s %s", firstName, lastName, familyName);
    }
}
