package ru.zav.storedbooksinfo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByGenre(Genre genre);
    List<Book> findByTitle(String title);
}
