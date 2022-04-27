package ru.zav.storedbooksinfo.dao;

import ru.zav.storedbooksinfo.domain.BookComment;

import java.util.List;

public interface BookCommentRepository {
    BookComment getById(String id);
    int deleteById(String id);
    BookComment save(BookComment comment);
    List<BookComment> readAll() ;
    void clearAll() ;
}
