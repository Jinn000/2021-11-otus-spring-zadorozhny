package ru.zav.storedbooksinfo.service;

import ru.zav.storedbooksinfo.datatypes.BookBean;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book add(BookBean bookBean) throws AppServiceException;
    int delete(String bookId) throws AppServiceException;
    Book changeTitle(String bookId, String newTitle) throws AppServiceException;
    List<Book> getAll() throws AppServiceException;
    List<Book> findByTitle(String title) throws AppServiceException;
    Optional<Book> addComment(String bookId, String comment) throws AppServiceException;
    Optional<Book> deleteComment(String commentId) throws AppServiceException;
    Optional<Book> updateComment(String commentId, String newComment) throws AppServiceException;
    List<Book> readComments(String bookId) throws AppServiceException;
}
