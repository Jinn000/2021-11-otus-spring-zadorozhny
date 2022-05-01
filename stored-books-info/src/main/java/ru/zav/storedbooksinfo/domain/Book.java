package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Document(collection = "book")
public class Book {
    @Id
    private String id;
    private String title;
    private Genre genre = null;
    private List<Author> authors;
    private List<BookComment> comments;

    public static Book generateBook(String title, Genre genre, List<Author> authors, List<BookComment> comments) {
        final Book book = new Book();
        try {
            book.setId(UUID.randomUUID().toString());
            book.setTitle(title);
            book.setGenre(genre);
            book.setAuthors(authors);
            book.setComments(comments);
        } catch (Exception e) {
            throw new AppServiceException(String.format("не удалось сгенерить объект Book. Причина: %s", e.getCause()), e);
        }

        return book;
    }

    public String getAuthorsString(){
        final List<String> authorsFullNames = authors.stream()
                .map(Author::toString)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        return String.join(", ", authorsFullNames);
    }
}
