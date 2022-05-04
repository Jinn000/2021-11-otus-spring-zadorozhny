package ru.zav.storedbooksinfo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.zav.storedbooksinfo.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String> {

    @Query("{'firstName' : :#{#firstName}, 'lastName' : :#{#lastName}, 'familyName' : :#{#familyName}}")
    Optional<Author> findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("familyName") String familyName);
    List<Author> findByFirstName(String firstName);
    List<Author> findByFamilyName(String familyName);
}
