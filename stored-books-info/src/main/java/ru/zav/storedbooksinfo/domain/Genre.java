package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
public class Genre {
    private String id;
    private String description;
}
