package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Document(collection = "book_comment")
public class BookComment {
    @Id
    private String id;
    private String name;
    @DBRef
    private Book book;
    private String comment;

    @Override
    public String toString() {
        return String.format("%s %s", name, comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BookComment that = (BookComment) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(book.getId(), that.book.getId())
                .append(comment, that.comment)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(book.getId())
                .append(comment)
                .toHashCode();
    }
}
