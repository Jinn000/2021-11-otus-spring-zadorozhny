package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "BOOK")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "TITLE", length = 256)
    private String title;

    @ManyToOne
    @JoinColumn(name = "GENRE_ID")
    private Genre genre = null;

    @ManyToMany(targetEntity = Author.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "BOOK_AUTHOR"
            , joinColumns = @JoinColumn(name = "BOOK_ID")
            , inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID")
    )
    private List<Author> authors;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, orphanRemoval = true)
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
