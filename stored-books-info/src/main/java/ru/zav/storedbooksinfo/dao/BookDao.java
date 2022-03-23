package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> getById(String id);
    int deleteById(String id);
    int insert(Book book);
    List<Book> readAll();
    void clearAll();
    Optional<String> findAuthorSetIdByBookId(String id);
    List<Book> findByGenre(Genre genre);
    List<Book> findByTitle(String title);
}
