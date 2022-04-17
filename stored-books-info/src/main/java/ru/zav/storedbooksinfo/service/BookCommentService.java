package ru.zav.storedbooksinfo.service;

import ru.zav.storedbooksinfo.datatypes.BookBean;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.BookComment;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.Optional;

public interface BookCommentService {
    Optional<Book> addComment(String bookId, String comment) throws AppServiceException;
    Optional<Book> deleteComment(String commentId) throws AppServiceException;
    Optional<Book> updateComment(String commentId, String newComment) throws AppServiceException;
    List<BookComment> readComments(String bookId) throws AppServiceException;
}