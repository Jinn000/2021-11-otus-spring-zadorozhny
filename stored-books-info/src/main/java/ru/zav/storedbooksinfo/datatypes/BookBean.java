package ru.zav.storedbooksinfo.datatypes;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import ru.zav.storedbooksinfo.domain.Author;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class BookBean {
    private String title;
    private List<Author> authors;
    private String genreTitle;

    public void setTitle(String title) {
        this.title = StringUtils.trim(title);
    }

    public void setGenreTitle(String genreTitle) {
        this.genreTitle = StringUtils.trim(genreTitle);
    }

    @Override
    public String toString(){
        return String.format("%s. Жанр: %s. Автор: %s", this.title, this.genreTitle, this.authors.stream().map(Author::toString).collect(Collectors.toList()));
    }
}
