package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "BOOK_COMMENT")
public class BookComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "NAME", length = 64)
    private String name;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID",nullable = false)
    private Book book;

    @Column(name = "COMMENT", length = 4000)
    private String comment;

    @Override
    public String toString() {
        return String.format("%s %s", name, comment);
    }
}
