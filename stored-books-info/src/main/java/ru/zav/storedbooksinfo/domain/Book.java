package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import ru.zav.storedbooksinfo.utils.AppDomainException;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

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

    @OneToOne(targetEntity = Genre.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "GENRE_ID")
    private Genre genre = null;

    @ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "BOOK_AUTHOR"
            , joinColumns = @JoinColumn(name = "BOOK_ID")
            , inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID")
    )
    private List<Author> authors;

    @OneToMany(mappedBy="bookId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookComment> comments;

    public static Book generateBook(String title, Genre genre, List<Author> authors, List<BookComment> comments) throws AppDomainException {
        final Book book = new Book();
        try {
            book.setId(UUID.randomUUID().toString());
            book.setTitle(title);
            book.setGenre(genre);
            book.setAuthors(authors);
            book.setComments(comments);
        } catch (Exception e) {
            throw new AppDomainException(String.format("не удалось сгенерить объект Book. Причина: %s", e.getCause()), e);
        }

        return book;
    }
}
