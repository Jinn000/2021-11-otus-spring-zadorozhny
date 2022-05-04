package ru.zav.storedbooksinfo.service;

import ru.zav.storedbooksinfo.datatypes.BookBean;
import ru.zav.storedbooksinfo.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book add(BookBean bookBean);
    int delete(String bookId);
    Book changeTitle(String bookId, String newTitle);
    List<Book> getAll();
    List<Book> findByTitle(String title);
    Optional<Book> findById(String id);
}
