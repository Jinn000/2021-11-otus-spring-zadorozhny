package ru.zav.storedbooksinfo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.zav.storedbooksinfo.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String> {
    Optional<Genre> findByDescription(String description);
}
