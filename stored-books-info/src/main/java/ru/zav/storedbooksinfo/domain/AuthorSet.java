package ru.zav.storedbooksinfo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthorSet {
    private String id;
    private String authorSetId;
    private Author author;
}
