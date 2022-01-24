package ru.zav.storedbooksinfo.dao;

import lombok.Getter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.AuthorSet;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class BookDaoJdbc implements BookDao{
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private GenreDao genreDao;
    private AuthorDao authorDao;
    private AuthorSetDao authorSetDao;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations, GenreDao genreDao, AuthorDao authorDao, AuthorSetDao authorSetDao) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.genreDao = genreDao;
        this.authorDao = authorDao;
        this.authorSetDao = authorSetDao;
    }

    /**Получение Book по ID
     * @return Объект AuthorSet*/
    @Override
    public Book getById(String id) throws AppDaoException {
        final Map<String, String> parameters = Map.of("id", id);
        final String sql = "SELECT b.id, b.title, b.genre_id, b.authors_set_id, FROM BOOK b WHERE b.id = :id";
        final Book book;
        try {
            book = namedParameterJdbcOperations.queryForObject(sql, parameters, new BookMapper());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return book;
    }

    /**Удаление по ID
     * @return количество удаленных строк*/
    @Override
    public int deleteById(String id) throws AppDaoException {
        final Map<String, String> parameters = Map.of("id", id);
        final String sql = "DELETE FROM BOOK b WHERE b.id = :id";

        try {
            return namedParameterJdbcOperations.update(sql, parameters);
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось удалить объект с ID: %s. Причина: %s", id,  e.getCause()), e);
        }
    }

    /**Вставка обьекта.
     * @return количество добавленных строк*/
    @Override
    public int insert(Book book) throws AppDaoException {
        final String authorId;
        try {
            authorId = book.getAuthors().get(0).getId();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось определить автора книги. Причина: %s", e.getCause()));
        }
        final List<AuthorSet> authorSetList = authorSetDao.findByAuthorId(authorId);

        if(authorSetList.isEmpty()) throw new AppDaoException(String.format("Не удалось найти перечень авторов по ID автора: %s", authorId));

        String authorSetId = authorSetList.get(0).getAuthorSetId();
        if(authorSetId == null) throw new AppDaoException(String.format("Не определен ID перечня авторов книги с ID: %s", book.getId()));

        final Map<String, String> parameters = Map.of(
                "id", book.getId()
                , "title", book.getTitle()
                , "genreId", book.getGenre().getId()
                ,"authorsSetId", authorSetId);

        final String sql = "INSERT INTO BOOK (id, title, genre_id, authors_set_id) values (:id, :title, :genreId, :authorsSetId)";

        try {
            return namedParameterJdbcOperations.update(sql, parameters);
        } catch (Exception e) {
            throw new AppDaoException(String.format("Вставка обьекта не удалась. Причина: %s", e.getCause()), e);
        }
    }

    /**Получение всего содержимого таблицы*/
    @NotNull
    @Override
    public List<Book> readAll() throws AppDaoException {
        final String sql = "SELECT b.id, b.title, b.genre_id, b.authors_set_id FROM BOOK b";
        final List<Book> bookList;
        try {
            bookList = namedParameterJdbcOperations.query(sql, new BookMapper());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return bookList;
    }

    /**Очистка таблицы*/
    @Override
    public void clearAll() throws AppDaoException {
        final String sql = "TRUNCATE TABLE BOOK";
        try {
            namedParameterJdbcOperations.update(sql, Map.of());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось сбросить таблицу. Причина: %s", e.getCause()), e);
        }
    }



    @Getter
    final class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("id");
            String title = rs.getString("title");
            String genreId = rs.getString("genre_id");
            String authorsSetId = rs.getString("authors_set_id");

            Genre genre;
            List<Author> authorList;

            try {
                genre = genreDao.getById(genreId);
            } catch (AppDaoException e) {
                throw new SQLException(String.format("Не удалось найти Genre по ID: %s. Причина: %s", genreId, e.getCause()), e);
            }

            final List<AuthorSet> authorSetList;
            try {
                authorSetList = authorSetDao.findByAuthorsSetId(authorsSetId);
            } catch (AppDaoException e) {
                throw new SQLException(String.format("Не удалось найти перечень AuthorSet по ID: %s. Причина: %s", authorsSetId, e.getCause()), e);
            }

            authorList = authorSetList.stream()
                    .map(AuthorSet::getAuthor)
                    .collect(Collectors.toList());

            return new Book(id, title, genre, authorList);
        }
    }
}
