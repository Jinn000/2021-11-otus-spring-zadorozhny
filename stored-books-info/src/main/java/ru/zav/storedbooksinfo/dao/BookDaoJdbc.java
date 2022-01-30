package ru.zav.storedbooksinfo.dao;

import lombok.Getter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.zav.storedbooksinfo.dao.datatypes.EntityId;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.AuthorSet;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.UuidGeneratorNoDashes;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public Optional<Book> getById(String id) throws AppDaoException {
        final Map<String, String> parameters = Map.of("id", id);
        final String sql = "SELECT b.id, b.title, b.genre_id, b.authors_set_id FROM BOOK b WHERE b.id = :id";
        Optional<Book> bookOptional;

        try {
            bookOptional = Optional.ofNullable(namedParameterJdbcOperations.queryForObject(sql, parameters, new BookMapper()));
        }
        catch (EmptyResultDataAccessException e) {
            bookOptional = Optional.empty();
        }
        catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return bookOptional;
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

        Optional<String> authorSetByBookId = findAuthorSetIdByBookId(book.getId());

        try {
            if(authorSetByBookId.isPresent()){
                /*сбросить текущий набор авторов*/
                final List<AuthorSet> authorSets = authorSetDao.findByAuthorsSetId(authorSetByBookId.get());
                authorSets.forEach(authorSet -> {
                    try {
                        authorSetDao.deleteById(authorSet.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                /*набрать новый перечень авторов для книги*/
                final String setId = authorSetByBookId.get();
                book.getAuthors().forEach(author -> {
                    try {
                        authorSetDao.insert(new AuthorSet(new UuidGeneratorNoDashes().generateUuid(), setId, author));
                    } catch (AppDaoException e) {
                        e.printStackTrace();
                    }
                });
            }else {
                String newSetId = new UuidGeneratorNoDashes().generateUuid();
                authorSetByBookId = Optional.ofNullable(newSetId);
                book.getAuthors().forEach(author -> {
                    try {
                        authorSetDao.insert(new AuthorSet(new UuidGeneratorNoDashes().generateUuid(), newSetId, author));
                    } catch (AppDaoException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось добавить автора к перечню авторов книги %s. Причина: %s", book.getId(), e.getCause()), e);
        }
/*        authorSetByBookId.map(setId -> book.getAuthors().forEach(author -> {
            try {
                authorSetDao.insert(new AuthorSet(new UuidGeneratorNoDashes().generateUuid(), setId, author));
            } catch (AppDaoException e) {
                e.printStackTrace();
            }
        })).orElse(() -> {
            String newSetId = new UuidGeneratorNoDashes().generateUuid();
            book.getAuthors().forEach(author -> {
                try {
                    authorSetDao.insert(new AuthorSet(new UuidGeneratorNoDashes().generateUuid(), newSetId, author));
                } catch (AppDaoException e) {
                    e.printStackTrace();
                }
            });
        });*/

        if(authorSetByBookId.isEmpty()) throw new AppDaoException(String.format("Не определен ID перечня авторов книги с ID: %s", book.getId()));

        final Map<String, String> parameters = Map.of(
                "id", book.getId()
                , "title", book.getTitle()
                , "genreId", book.getGenre().getId()
                ,"authorsSetId", authorSetByBookId.get());

        final String sql = getById(book.getId()).isEmpty()
                ? "INSERT INTO BOOK (id, title, genre_id, authors_set_id) values (:id, :title, :genreId, :authorsSetId)"
                : "UPDATE BOOK SET title=:title, genre_id=:genreId, authors_set_id=:authorsSetId WHERE id = :id";

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

    @Override
    public Optional<String> findAuthorSetIdByBookId(String id) throws AppDaoException {
        final Map<String, String> parameters = Map.of("id", id);
        final String sql = "SELECT b.authors_set_id FROM BOOK b WHERE b.id = :id";
        final List<String> authorSetIdList;
        try {
            authorSetIdList = namedParameterJdbcOperations.query(sql, parameters, new StringMapper());
        } catch (Exception e) {
            return Optional.empty();
        }

        Optional<String> authorsSetId;

        if(authorSetIdList.isEmpty()){
            authorsSetId = Optional.empty();
        } else if(authorSetIdList.size() >1){
            throw new AppDaoException(String.format("Найдено более одного набора авторов у книги с ID: %s", id));
        } else {
            authorsSetId = Optional.ofNullable(authorSetIdList.get(0));
        }
        return authorsSetId;

    }


    @Getter
    final class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("id");
            String title = rs.getString("title");
            EntityId genreId = new EntityId(rs.getString("genre_id"));
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

    static final class StringMapper implements RowMapper<String>{

        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {

            return rs.getString("authors_set_id");
        }
    }
}
