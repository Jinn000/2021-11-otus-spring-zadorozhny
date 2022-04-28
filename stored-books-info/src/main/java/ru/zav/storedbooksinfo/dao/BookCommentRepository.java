package ru.zav.storedbooksinfo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.zav.storedbooksinfo.domain.BookComment;

import java.util.List;

public interface BookCommentRepository extends JpaRepository<BookComment, String> {
    List<BookComment> findByCommentContaining(@Param("word") String word);
    List<BookComment> findByNameContaining(@Param("name") String name);
}
