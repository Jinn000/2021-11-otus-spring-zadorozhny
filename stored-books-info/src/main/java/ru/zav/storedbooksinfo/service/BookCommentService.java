package ru.zav.storedbooksinfo.service;

import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentService {
    Optional<Book> addComment(String bookId, String comment);
    Optional<Book> deleteComment(String commentId);
    Optional<Book> updateComment(String commentId, String newComment);
    List<BookComment> readComments(String bookId);
}
