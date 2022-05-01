package ru.zav.storedbooksinfo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.zav.storedbooksinfo.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String> {

    @Query("SELECT a FROM Author a WHERE a.firstName = :firstName AND a.lastName = :lastName AND a.familyName = :familyName")
    Optional<Author> findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("familyName") String familyName);
}
