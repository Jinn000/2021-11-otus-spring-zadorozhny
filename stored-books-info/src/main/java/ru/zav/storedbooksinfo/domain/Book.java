package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.zav.storedbooksinfo.utils.AppDomainException;
import ru.zav.storedbooksinfo.utils.UuidGeneratorNoDashes;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
public class Book {
    private String id;
    private String title;
    private Genre genre;
    private List<Author> authors;

    public static Book generateBook(String title, Genre genre, @NotNull List<Author> authors) throws AppDomainException {
        final Book book = new Book();
        try {
            book.setId(new UuidGeneratorNoDashes().generateUuid());
            book.setTitle(title);
            book.setGenre(genre);
            book.setAuthors(authors);
        } catch (Exception e) {
            throw new AppDomainException(String.format("не удалось сгенерить объект Book. Причина: %s", e.getCause()), e);
        }

        return book;
    }
}
