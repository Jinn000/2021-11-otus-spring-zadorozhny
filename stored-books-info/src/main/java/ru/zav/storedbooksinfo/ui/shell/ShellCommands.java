package ru.zav.storedbooksinfo.ui.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.zav.storedbooksinfo.dao.AuthorDao;
import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.service.AuthorService;
import ru.zav.storedbooksinfo.service.BooksInfoUi;
import ru.zav.storedbooksinfo.service.GenreService;
import ru.zav.storedbooksinfo.ui.LayoutService;
import ru.zav.storedbooksinfo.utils.AppServiceException;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands implements BooksInfoUi {
    public static final String CANT_SHOW_GENRES_S = "Не удалось отобразить перечень жанров. Причина: %s";
    public static final String CANT_SHOW_AUTHORS_S = "Не удалось отобразить перечень авторов. Причина: %s";
    private final LayoutService<String, String> layoutService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final AuthorDao authorDao;



    @ShellMethodAvailability(value = "false")
    @ShellMethod(key = {"genre-add-param", "gap"}, value = "Добавление жанра (с параметром)")
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
    @Override
    public void genreAdd() {
        final String newGenre = layoutService.ask("Введите жанр.");
        genreAdd(newGenre);
    }

    @ShellMethod(key = {"genre-delete", "gd"}, value = "Удаление жанра")
    @ShellMethodAvailability(value = "true")
    @Override
    public void genreDelete(){
        layoutService.show("Удаление жанра.");
        final String genreDescription = layoutService.ask("Введите жанр для удаления.");
        try {
            final int deleteCount = genreService.delete(genreDescription);
            if(deleteCount < 1) layoutService.show(String.format("Жанр %s не удалось удалить.", genreDescription));
        } catch (AppServiceException e) {
            layoutService.show(String.format("Не удалось удалить жанр %s. Причина: %s", genreDescription, e.getCause()));
        }
    }

    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"genres","gs"}, value = "Просмотр списка жанров")
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


    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"author-add", "aa"}, value = "Добавление автора")
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
    @ShellMethod(key = {"authors", "as"}, value = "Показать всех авторов")
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
                        return String.format("%d. %s %s %s", counter[0], author.getFirstName(), author.getLastName(), author.getFamilyName());
                    })
                    .forEach(layoutService::show);
        }
    }
}
