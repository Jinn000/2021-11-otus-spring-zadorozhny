package ru.zav.storedbooksinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "AUTHORS_SET")
public class AuthorSet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "AUTHORS_SET_ID")
    private String authorsSetId;

    @ManyToOne(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "AUTHOR_ID", referencedColumnName = "id")
    private Author author;
}
