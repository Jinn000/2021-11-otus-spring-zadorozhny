package ru.zav.storedbooksinfo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import ru.zav.storedbooksinfo.domain.BookComment;

import java.util.List;

public interface BookCommentRepository extends MongoRepository<BookComment, String>, BookCommentRepositoryCustom {
    List<BookComment> findByCommentContaining(@Param("word") String word);
    List<BookComment> findByNameContaining(@Param("name") String name);
}
