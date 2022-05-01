package ru.zav.storedbooksinfo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByGenre(Genre genre);
    List<Book> findByTitle(String title);
}
