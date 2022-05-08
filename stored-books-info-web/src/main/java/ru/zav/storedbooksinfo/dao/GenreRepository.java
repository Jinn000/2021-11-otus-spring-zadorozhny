package ru.zav.storedbooksinfo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zav.storedbooksinfo.dao.datatypes.EntityId;
import ru.zav.storedbooksinfo.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, String> {
    Optional<Genre> findByDescription(String description);
}
