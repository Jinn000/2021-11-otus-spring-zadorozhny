package ru.zav.storedbooksinfo.ui.shell;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.datatypes.BookBean;
import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.BookComment;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.service.*;
import ru.zav.storedbooksinfo.ui.LayoutService;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands implements BooksInfoUi {
    public static final String CANT_SHOW_GENRES_S = "Не удалось отобразить перечень жанров. Причина: %s";
    public static final String CANT_SHOW_AUTHORS_S = "Не удалось отобразить перечень авторов. Причина: %s";
    public static final String CANT_SHOW_BOOKS_S = "Не удалось отобразить перечень книг. Причина: %s";
    public static final String ACTION_CANCELLED = "Действие отменено. \n";
    private final LayoutService<String, String> layoutService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BookCommentService bookCommentService;


    //---------------------------------------------
    // Жанры

    @ShellMethodAvailability(value = "false")
    @ShellMethod(key = {"genre-add-param", "gap"}, value = "Добавление жанра (с параметром)")
    @Transactional
    @Override
    public void genreAdd(String description){
        Genre newGenre = null;
        try {
             newGenre = genreService.add(description);
        } catch (AppServiceException e) {
            layoutService.show(e.getMessage());
        }

        if(newGenre == null){
            layoutService.show("Не удалось добавить жанр.");
        }else {
            layoutService.show(String.format("Добавлен жанр %s", newGenre.getDescription()));
        }

    }

    @ShellMethod(key = {"genre-add", "ga"}, value = "Добавление жанра")
    @ShellMethodAvailability(value = "true")
    @Transactional
    @Override
    public void genreAdd() {
        final String newGenre = layoutService.ask("Введите жанр.");
        genreAdd(newGenre);
    }

    @ShellMethod(key = {"genre-delete", "gd"}, value = "Удаление жанра")
    @ShellMethodAvailability(value = "true")
    @Transactional
    @Override
    public void genreDelete(){
        layoutService.show("Удаление жанра.");
        final String genreDescription = layoutService.ask("Введите жанр для удаления.");
        try {
            final int deleteCount = genreService.delete(genreDescription);
            if(deleteCount < 1) layoutService.show(String.format("Жанр %s не удалось удалить.", genreDescription));
            else layoutService.show(String.format("Жанр %s удален.", genreDescription));
        } catch (AppServiceException e) {
            layoutService.show(String.format("Не удалось удалить жанр %s. Причина: %s", genreDescription, e.getCause()));
        }
    }

    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"genres","gs"}, value = "Просмотр списка жанров")
    @Transactional(readOnly = true)
    @Override
    public void showGenres() {
        final List<Genre> genreList;
        try {
            genreList = genreService.getAll();
        } catch (AppServiceException e) {
            layoutService.show(String.format(CANT_SHOW_GENRES_S, e.getCause()));
            return;
        }

        if(genreList.isEmpty()){
            layoutService.show("Нет ни одного жанра.");
        }else{
            layoutService.show("Доступные жанры книг: ");
            int[] counter = {0};
            genreList.stream()
                    .map(genre -> {
                        counter[0]++;
                        return counter[0] + ". " + genre.getDescription();
                    })
                    .forEach(layoutService::show);
        }
    }

    //-----------------------------------------
    // Авторы

    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"author-add", "aa"}, value = "Добавление автора")
    @Transactional
    @Override
    public void authorAdd() {
        layoutService.show("Добавление нового автора.");
        final String firstName = layoutService.ask("Введите имя автора.");
        final String lastName = layoutService.ask("Введите отчество автора.");
        final String familyName = layoutService.ask("Введите фамилию автора.");

        final FullName fullName = FullName.builder().firstName(firstName)
                .lastName(lastName)
                .familyName(familyName)
                .build();
        try {
            final Author author = authorService.add(fullName);

            if(author != null) layoutService.show(String.format("Добавлен автор %s %s %s.", author.getFirstName(), author.getLastName(), author.getFamilyName()));
        } catch (AppServiceException e) {
            layoutService.show(String.format("Не удалось добавить автора %s. Причина: %s", fullName.toString(), e.getLocalizedMessage()));
        }

    }

    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"author-rename", "ar"}, value = "Переименование автора")
    @Transactional
    @Override
    public void authorRename(){
        layoutService.show("Переименование автора.");

        boolean doRepeat = true;
        while (doRepeat){
            String namesString = layoutService.ask("Введите полное ФИО автора для переименования (в формате Имя Отчество Фамилия). Или \"0\", для отмены.");
            if (namesString.equals("0")) {
                doRepeat = false;
                continue;
            }


            FullName fullName;
            try {
                fullName = FullName.valueOf(namesString);
            } catch (AppServiceException e) {
                layoutService.show(e.getLocalizedMessage());
                continue;
            }

            final String newNamesString = layoutService.ask("Введите новое имя автора (в формате Имя Отчество Фамилия):");

            FullName newFullName;
            try {
                newFullName = FullName.valueOf(newNamesString);
            } catch (AppServiceException e) {
                layoutService.show(e.getLocalizedMessage());
                continue;
            }

            Optional<Author> byFullNameOpt = Optional.empty();
            try {
                byFullNameOpt = Optional.ofNullable(authorService.rename(fullName, newFullName));
            } catch (AppServiceException e) {
                layoutService.show(e.getLocalizedMessage());
            }

            doRepeat = byFullNameOpt.isEmpty();
            if(doRepeat){
                layoutService.show("Не удалось переименовать автора. Повторите ввод.");
            }else{
                layoutService.show("Автор переименован.");
            }
        }
    }

    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"authors", "as"}, value = "Показать всех авторов")
    @Transactional(readOnly = true)
    @Override
    public void showAuthors() {
        final List<Author> authorList;
        try {
            authorList = authorService.getAll();
        } catch (AppServiceException e) {
            layoutService.show(String.format(CANT_SHOW_AUTHORS_S, e.getCause()));
            return;
        }

        if(authorList.isEmpty()){
            layoutService.show("Нет ни одного автора.");
        }else{
            layoutService.show("Перечень авторов книг: ");
            int[] counter = {0};
            authorList.stream()
                    .map(author -> {
                        counter[0]++;
                        return String.format("%d. %s", counter[0], author.toString());
                    })
                    .forEach(layoutService::show);
        }
    }
    //------------------------------
    // Книги

    @ShellMethod(key = {"book-add", "ba"}, value = "Добавить книгу")
    @ShellMethodAvailability(value = "true")
    @Transactional
    @Override
    public void bookAdd() {
        layoutService.show("Добавление книги.");

        //Ввести наименование
        final String bookTitle = askString("Введите название книги. Или введите пробел для отмены.");
        if(StringUtils.isBlank(bookTitle)) {
            layoutService.show(ACTION_CANCELLED);
            return;
        }

        //Ввести жанр
        layoutService.show("Введите жанр.");
        Genre genre;

        final String genreToBook = askString("Введите жанр. Или введите пробел для отмены.");
        if(StringUtils.isBlank(genreToBook)) {
            layoutService.show(ACTION_CANCELLED);
            return;
        }
        else {
            try {
                Optional<Genre> genreOptional = genreService.findByDescription(genreToBook);
                if(genreOptional.isEmpty()) {
                    try {
                        genre = genreService.add(genreToBook);
                    } catch (AppServiceException e) {
                        layoutService.show(String.format("Ошибка. Не удалось создать жанр при добавлении книги. Причина: %s", e.getLocalizedMessage()));
                        return;
                    }
                }else {
                    genre = genreOptional.get();
                }
            } catch (AppServiceException e) {
                layoutService.show(String.format("Не удалось добавить книгу %s. Причина: %s", bookTitle, e.getCause()));
                return;
            }
        }

        //Ввести перечень авторов
        layoutService.show("Введите перечень авторов.");
        List<Author> authorList = new ArrayList<>();

        final String authorFullName = askString("Введите Фамилию Имя Отчество автора. Для отмены введите пробел. Для окончания ввода авторов введите 0.");
        if(StringUtils.isBlank(authorFullName)){
            layoutService.show(ACTION_CANCELLED);
            return;
        }else {
            final String[] names = authorFullName.split(" ");
            final FullName fullName = FullName.builder()
                    .familyName(names[0])
                    .firstName(names[1])
                    .lastName(names[2])
                    .build();
            try {
                final Optional<Author> authorOpt = authorService.findByFullName(fullName);
                if(authorOpt.isEmpty()){
                    //создание нового автора
                    final Author newAuthor = authorService.add(fullName);
                    authorList.add(newAuthor);
                }else {
                    authorList.add(authorOpt.get());
                }

            } catch (AppServiceException e) {
                layoutService.show(String.format("Ошибка. Не удалось добавить автора при добавлении книги. Причина: %s", e.getLocalizedMessage()));
                return;
            }
        }


        //Завершить сохранение книги
        try {
            final BookBean bookBean = BookBean.builder()
                    .title(bookTitle)
                    .genreTitle(genre.getDescription())
                    .authors(authorList)
                    .build();
            final Book addedBook = bookService.add(bookBean);
            if(addedBook != null) layoutService.show(String.format("Добавлена книга %s", addedBook.getTitle()));
        } catch (AppServiceException e) {
            layoutService.show(String.format("Ошибка. Не удалось добавить книгу. Причина: %s", e.getLocalizedMessage()));
        }
    }

    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"book-delete", "bd"}, value = "Удаление книги")
    @Transactional
    @Override
    public void bookDelete() {
        layoutService.show("Удаление книги.");
        final String bookTitle = layoutService.ask("Введите название для удаления.");
        final List<Book> bookList;
        try {
            bookList = bookService.findByTitle(bookTitle);
        } catch (AppServiceException e) {
            layoutService.show(String.format("Не удалось удалить книгу %s. Причина: %s", bookTitle, e.getCause()));
            return;
        }

        if(bookList.isEmpty()) {
            layoutService.show("Не найдено ни одной книги.");
        }
        else{
            layoutService.show("Найдены следующие книги:");
            int[] counter = {0};
            bookList.stream()
                    .map(book -> {
                        counter[0]++;
                        return String.format("%d. %s. [%s]", counter[0], book.getTitle(), book.getGenre().getDescription());
                    })
                    .forEach(layoutService::show);

            int itemToDelete = askItemNumberToAction(counter[0]);
            if(itemToDelete == 0) {
                layoutService.show(ACTION_CANCELLED);
            }else {
                final String bookToDeleteId = bookList.get(itemToDelete - 1).getId();
                try {
                    final int deletedCount = bookService.delete(bookToDeleteId);
                    if (deletedCount > 0) {
                        layoutService.show("Книга удалена.");
                    } else {
                        layoutService.show("Книга удалить не удалось.");
                    }
                } catch (AppServiceException e) {
                    layoutService.show(String.format("Не удалось удалить книгу %s. Причина: %s", bookList.get(itemToDelete - 1).getTitle(), e.getCause()));
                }
            }

        }
    }

    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"book-rename", "br"}, value = "Переименование книги")
    @Transactional
    @Override
    public void bookRename() {
        layoutService.show("Переименование книги.");
        final String bookTitle = layoutService.ask("Введите название для переименования.");
        final List<Book> bookList;
        try {
            bookList = bookService.findByTitle(bookTitle);
        } catch (AppServiceException e) {
            layoutService.show(String.format("Не удалось переименовать книгу %s. Причина: %s", bookTitle, e.getCause()));
            return;
        }

        if(bookList.isEmpty()) {
            layoutService.show("Не найдено ни одной книги.");
        }
        else{
            layoutService.show("Найдены следующие книги:");
            final int[] counter = {0};
            bookList.stream()
                    .map(book -> {
                        counter[0]++;
                        return String.format("%d. %s. [%s]", counter[0], book.getTitle(), book.getGenre().getDescription());
                    })
                    .forEach(layoutService::show);

            final int itemToRename = askItemNumberToAction(counter[0]);
            if(itemToRename == 0) {
                layoutService.show(ACTION_CANCELLED);
            }else {
                final String bookToRenameId = bookList.get(itemToRename - 1).getId();
                final String oldTitle = bookList.get(itemToRename - 1).getTitle();
                try {
                    final String newTitle = layoutService.ask("Введите новое название. Введите 0 для отмены.");

                    // Возможность отмены
                    boolean isCancel = false;
                    try {
                        isCancel = Integer.parseInt(newTitle) == 0;
                    }
                    catch (NumberFormatException ignored) {
                    }

                    if(isCancel){
                        layoutService.show(ACTION_CANCELLED);
                        return;
                    }

                    final Book renamedBook = bookService.changeTitle(bookToRenameId, newTitle);
                    layoutService.show(String.format("Книга переименована с [%s] на [%s]", oldTitle, renamedBook.getTitle()));
                } catch (AppServiceException e) {
                    layoutService.show(String.format("Не удалось переименовать книгу %s. Причина: %s", oldTitle, e.getCause()));
                }
            }

        }
    }

    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"book-show", "bs"}, value = "Показать все книги")
    @Transactional(readOnly = true)
    @Override
    public void showBooks() {
        final List<Book> bookList;
        try {
            bookList = bookService.getAll();
        } catch (AppServiceException e) {
            layoutService.show(String.format(CANT_SHOW_BOOKS_S, e.getCause()));
            return;
        }

        if(bookList.isEmpty()){
            layoutService.show("Нет ни одной книги.");
        }else{
            layoutService.show("Перечень книг: ");
            int[] counter = {0};
            bookList.stream()
                    .map(book -> {
                        counter[0]++;
                        return String.format("%d. %s. [%s] <%s>", counter[0], book.getTitle(), book.getGenre().getDescription(), book.getAuthorsString());
                    })
                    .forEach(layoutService::show);

            bookCommentsShow();
        }
    }

    private int askItemNumberToAction(int itemsCount){
        final String answer = layoutService.ask(String.format("Выберите пункт для выполнения действия. Введите число от 1 до %d. Для отмены выберите 0. \n", itemsCount));

        int itemNumber;
        try {
            itemNumber = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            layoutService.show("Нет. Введено не число.");
            itemNumber = askItemNumberToAction(itemsCount);
        }

        if(itemNumber == 0){
            return 0;
        }else if(itemNumber<1 || itemNumber>itemsCount) {
            layoutService.show(String.format("Введено не верное число. Введите число от 1 до %d. Для отмены введите 0", itemsCount));
            itemNumber = askItemNumberToAction(itemsCount);
        }

        return itemNumber;
    }
    private String askString(String msg){
        String answer = layoutService.ask(msg);

        if(answer.equals(" ")){
            return " ";
        }else if(StringUtils.isBlank(answer)) {
            layoutService.show("Введено не корректное наименование. Повторите ввод, или введите пробел для отмены.");
            answer = askString(msg);
        }
        return answer;
    }
//------------------------------
//--- Комментарии --------------
    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"comments-show", "cs"}, value = "Просмотр комментариев к книге")
    @Transactional
    @Override
    public void bookCommentsShow(){

        final List<Book> bookList;
        try {
            bookList = bookService.getAll();
        } catch (AppServiceException e) {
            layoutService.show(String.format(CANT_SHOW_BOOKS_S, e.getCause()));
            return;
        }

        if(bookList.isEmpty()){
            layoutService.show("Нет ни одной книги. Нечего комментировать.");
            return;
        }

        layoutService.show("Можно просмотреть комментарий к любой из книг. Выберите номер книги.");
        selectBookStage(bookList);
    }


    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"comments-add", "ca"}, value = "Добавление комментария")
    @Transactional
    @Override
    public void bookCommentAdd() {
        final List<Book> bookList;
        try {
            bookList = bookService.getAll();
        } catch (AppServiceException e) {
            layoutService.show(String.format(CANT_SHOW_BOOKS_S, e.getCause()));
            return;
        }

        if(bookList.isEmpty()){
            layoutService.show("Нет ни одной книги. Нечего комментировать.");
        }else{
            selectBookStage(bookList);
        }
    }

    private void selectBookStage(List<Book> bookList) {
        int[] counter = {0};

        boolean doSelectBook = true;
        while (doSelectBook){
            counter[0] = 0;
            bookList.stream()
                    .map(book -> {
                        counter[0]++;
                        return String.format("%d. %s. [%s]", counter[0], book.getTitle(), book.getGenre().getDescription());
                    })
                    .forEach(layoutService::show);

            layoutService.show("Можно добавить комментарий в одну из книг.");

            final int itemToComment = askItemNumberToAction(counter[0]);
            if(itemToComment == 0) {
                doSelectBook = false;
            }else {
                layoutService.show(String.format("Комментарии к книге %s: ", bookList.get(itemToComment - 1).getTitle()));
                bookList.get(itemToComment - 1).getComments().stream()
                        .map(BookComment::getComment)
                        .map(" - "::concat)
                        .forEach(layoutService::show);

                commentAddStage(bookList, itemToComment);
            }
        }

        layoutService.show(ACTION_CANCELLED);
    }

    private void commentAddStage(List<Book> bookList, int itemToComment) {
        boolean doCommentAdd = true;
        while (doCommentAdd){

            final String bookToCommentId = bookList.get(itemToComment - 1).getId();
            String newComment = "";
            try {
                newComment = layoutService.ask("Введите комментарий. Или 0 для отмены.");
                try {
                    doCommentAdd = Integer.parseInt(newComment) > 0;
                    if(!doCommentAdd){
                        continue;
                    }
                }
                catch (NumberFormatException ignored) {
                }

                final Optional<Book> optionalBook = bookCommentService.addComment(bookToCommentId, newComment);
                layoutService.show("Комментарий добавлен.");
                layoutService.show(String.format("Комментарии к книге %s: ", optionalBook.map(Book::getTitle).orElse(null)));
                optionalBook.map(Book::getComments).map(c-> {
                    c.stream()
                            .map(BookComment::getComment)
                            .map(" - "::concat)
                            .forEach(layoutService::show);
                    return c;
                });
            } catch (AppServiceException e) {
                layoutService.show(String.format("Не удалось добавить комментарий %s. Причина: %s", newComment, e.getCause()));
            }
        }

        layoutService.show(ACTION_CANCELLED);
    }
}
