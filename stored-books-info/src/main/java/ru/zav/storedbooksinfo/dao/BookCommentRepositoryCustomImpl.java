package ru.zav.storedbooksinfo.dao;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.zav.storedbooksinfo.domain.BookComment;

@RequiredArgsConstructor
public class BookCommentRepositoryCustomImpl implements BookCommentRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void removeCommentsByBookId(String bookId) {
        val query = Query.query(Criteria.where("$id").is(bookId));
        mongoTemplate.remove(query, BookComment.class);
    }
}
