package ru.zav.storedbooksinfo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Book {
    private String id;
    private String title;
    private Genre genre;
    private AuthorSet authorSet;
}
