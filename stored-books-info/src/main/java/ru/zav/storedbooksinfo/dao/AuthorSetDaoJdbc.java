package ru.zav.storedbooksinfo.dao;

import lombok.Getter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.AuthorSet;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class AuthorSetDaoJdbc implements AuthorSetDao{
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final AuthorDao authorDao;

    public AuthorSetDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations, AuthorDao authorDao) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.authorDao = authorDao;
    }

    /**Получение AuthorSet по ID
     * @return Объект AuthorSet*/
    @Override
    public AuthorSet getById(String id) throws AppDaoException {
        final Map<String, String> parameters = Map.of("id", id);
        final String sql = "SELECT a.id, a.authors_set_id, a.author_id, FROM AUTHORS_SET a WHERE a.id = :id";
        AuthorSet authorSet;
        try {
            authorSet = namedParameterJdbcOperations.queryForObject(sql, parameters, new AuthorSetMapper());
        } catch (EmptyResultDataAccessException emptyEx) {
            authorSet = null;
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return authorSet;
    }
    /**Получение коллекции AuthorSet по ID автора
     * @return Объект AuthorSet*/
    @NotNull
    @Override
    public List<AuthorSet> findByAuthorId(String authorId) throws AppDaoException {
        final Map<String, String> parameters = Map.of("authorId", authorId);
        final String sql = "SELECT a.id, a.authors_set_id, a.author_id, FROM AUTHORS_SET a WHERE a.author_id = :authorId";
        final List<AuthorSet> authorSetList;
        try {
            authorSetList = namedParameterJdbcOperations.query(sql, parameters, new AuthorSetMapper());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return authorSetList;
    }

    /**Получение коллекции AuthorSet по ID автора
     * @return Объект AuthorSet*/
    @NotNull
    @Override
    public List<AuthorSet> findByAuthorsSetId(String authorsSetId) throws AppDaoException {
        final Map<String, String> parameters = Map.of("authorsSetId", authorsSetId);
        final String sql = "SELECT a.id, a.authors_set_id, a.author_id, FROM AUTHORS_SET a WHERE a.authors_set_id = :authorsSetId";
        final List<AuthorSet> authorSetList;
        try {
            authorSetList = namedParameterJdbcOperations.query(sql, parameters, new AuthorSetMapper());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return authorSetList;
    }

    /**Удаление по ID
     * @return количество удаленных строк*/
    @Override
    public int deleteById(String id) throws AppDaoException {
        final Map<String, String> parameters = Map.of("id", id);
        final String sql = "DELETE FROM AUTHORS_SET a WHERE a.id = :id";

        try {
            return namedParameterJdbcOperations.update(sql, parameters);
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось удалить объект с ID: %s. Причина: %s", id,  e.getCause()), e);
        }
    }

    /**Вставка обьекта.
     * @return количество добавленных строк*/
    @Override
    public int insert(AuthorSet authorSet) throws AppDaoException {
        final Map<String, String> parameters = Map.of(
                "id", authorSet.getId()
                , "authors_set_id", authorSet.getAuthorSetId()
                ,"author_id", authorSet.getAuthor().getId());

        final String sql = "INSERT INTO AUTHORS_SET (id, authors_set_id, author_id) values (:id, :authors_set_id, :author_id)";

        try {
            return namedParameterJdbcOperations.update(sql, parameters);
        } catch (Exception e) {
            throw new AppDaoException(String.format("Вставка обьекта не удалась. Причина: %s", e.getCause()), e);
        }
    }

    /**Получение всего содержимого таблицы*/
    @NotNull
    @Override
    public List<AuthorSet> readAll() throws AppDaoException {
        final String sql = "SELECT a.id, a.authors_set_id, a.author_id FROM AUTHORS_SET a";
        final List<AuthorSet> authorSetList;
        try {
            authorSetList = namedParameterJdbcOperations.query(sql, new AuthorSetMapper());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return authorSetList;
    }

    /**Очистка таблицы*/
    @Override
    public void clearAll() throws AppDaoException {
        final String sql = "TRUNCATE TABLE AUTHORS_SET";
        try {
            namedParameterJdbcOperations.update(sql, Map.of());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось сбросить таблицу. Причина: %s", e.getCause()), e);
        }
    }



    @Getter
    final class AuthorSetMapper implements RowMapper<AuthorSet> {

        @Override
        public AuthorSet mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("id");
            String authorSetId = rs.getString("authors_set_id");
            String authorId = rs.getString("author_id");

            final Author author;
            try {
                author = authorDao.getById(authorId);
                return new AuthorSet(id, authorSetId, author);
            } catch (AppDaoException e) {
                throw new SQLException(String.format("Не удалось преобразовать ResultSet в объект. Причина: %s", e.getCause()), e);
            }
        }
    }
}
