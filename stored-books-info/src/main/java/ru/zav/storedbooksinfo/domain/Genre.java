package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Document(collection = "genre")
public class Genre {
    @Id
    private String id;
    private String description;
}