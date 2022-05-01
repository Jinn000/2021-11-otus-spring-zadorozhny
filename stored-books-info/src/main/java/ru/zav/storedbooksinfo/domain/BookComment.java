package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Document(collection = "book_comment")
public class BookComment {
    @Id
    private String id;
    private String name;
    private Book book;
    private String comment;

    @Override
    public String toString() {
        return String.format("%s %s", name, comment);
    }
}
