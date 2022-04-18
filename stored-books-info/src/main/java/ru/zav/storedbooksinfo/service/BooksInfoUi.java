package ru.zav.storedbooksinfo.service;

public interface BooksInfoUi {
    void genreAdd(String genreDescription);
    void genreAdd();
    void genreDelete();
    void showGenres();

    void authorAdd();
    void authorRename();
    void showAuthors();

    void bookAdd();
    void bookDelete();
    void bookRename();
    void showBooks();

    void bookCommentAdd();
    void bookCommentsShow();
}
