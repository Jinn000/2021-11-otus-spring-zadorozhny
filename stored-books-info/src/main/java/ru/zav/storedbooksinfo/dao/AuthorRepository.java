package ru.zav.storedbooksinfo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zav.storedbooksinfo.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, String> {

    Optional<Author> findByFirstNameAndLastNameAndFamilyName(String firstName, String lastName, String familyName);
}
