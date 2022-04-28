package ru.zav.storedbooksinfo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.zav.storedbooksinfo.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, String> {

    @Query("SELECT a FROM Author a WHERE a.firstName = :firstName AND a.lastName = :lastName AND a.familyName = :familyName")
    Optional<Author> findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("familyName") String familyName);
}
