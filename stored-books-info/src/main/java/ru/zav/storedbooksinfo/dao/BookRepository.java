package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {
    Optional<Book> getById(String id);
    int deleteById(String id);
    Book save(Book book);
    List<Book> readAll();
    void clearAll();
    List<Book> findByGenre(Genre genre);
    List<Book> findByTitle(String title);
}
