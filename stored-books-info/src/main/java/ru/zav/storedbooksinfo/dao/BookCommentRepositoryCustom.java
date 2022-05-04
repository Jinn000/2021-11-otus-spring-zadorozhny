package ru.zav.storedbooksinfo.dao;

public interface BookCommentRepositoryCustom {
    void removeCommentsByBookId(String bookId);
}
