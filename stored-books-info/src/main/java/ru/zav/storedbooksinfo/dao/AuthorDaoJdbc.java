package ru.zav.storedbooksinfo.dao;

import lombok.Getter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AuthorDaoJdbc implements AuthorDao{
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public AuthorDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    /**Получение Author по ID
     * @return Объект Author*/
    @Override
    public Author getById(String id) throws AppDaoException {
        final Map<String, String> parameters = Map.of("id", id);
        final String sql = "SELECT a.id, a.first_name, a.last_name, a.family_name FROM AUTHOR a WHERE a.id = :id";
        Author author;
        try {
            author = namedParameterJdbcOperations.queryForObject(sql, parameters, new AuthorMapper());
        } catch (EmptyResultDataAccessException emptyEx) {
            author = null;
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return author;
    }

    /**Удаление по ID
     * @return количество удаленных строк*/
    @Override
    public int deleteById(String id) throws AppDaoException {
        final Map<String, String> parameters = Map.of("id", id);
        final String sql = "DELETE FROM AUTHOR a WHERE a.id = :id";

        try {
            return namedParameterJdbcOperations.update(sql, parameters);
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось удалить объект с ID: %s. Причина: %s", id,  e.getCause()), e);
        }
    }

    /**Вставка обьекта.
     * @return количество добавленных строк*/
    @Override
    public int insert(Author author) throws AppDaoException {
        final Map<String, String> parameters = Map.of(
                "id", author.getId()
                , "firstName", author.getFirstName()
                ,"lastName", author.getLastName()
                ,"familyName", author.getFamilyName());

        final String sql = "INSERT INTO AUTHOR (id, first_name, last_name, family_name) values (:id, :firstName, :lastName, :familyName)";

        try {
            return namedParameterJdbcOperations.update(sql, parameters);
        } catch (Exception e) {
            throw new AppDaoException(String.format("Вставка обьекта не удалась. Причина: %s", e.getCause()), e);
        }
    }

    @Override
    public int update(Author author) throws AppDaoException {
        final Map<String, String> parameters = Map.of(
                "id", author.getId()
                , "firstName", author.getFirstName()
                ,"lastName", author.getLastName()
                ,"familyName", author.getFamilyName());

        final String sql = "UPDATE AUTHOR SET first_name = :firstName, last_name = :lastName, family_name = :familyName WHERE id = :id";

        try {
            return namedParameterJdbcOperations.update(sql, parameters);
        } catch (Exception e) {
            throw new AppDaoException(String.format("Редактирование обьекта не удалась. Причина: %s", e.getCause()), e);
        }
    }

    /**Получение всего содержимого таблицы*/
    @NotNull
    @Override
    public List<Author> readAll() throws AppDaoException {
        final String sql = "SELECT a.id, a.first_name, a.last_name, a.family_name FROM AUTHOR a";
        final List<Author> authorList;
        try {
            authorList = namedParameterJdbcOperations.query(sql, new AuthorMapper());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return authorList;
    }

    /**Очистка таблицы*/
    @Override
    public void clearAll() throws AppDaoException {
        final String sql = "TRUNCATE TABLE AUTHOR";
        try {
            namedParameterJdbcOperations.update(sql, Map.of());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось сбросить таблицу. Причина: %s", e.getCause()), e);
        }
    }

    @Override
    public Optional<Author> findByFullName(FullName fullName) throws AppDaoException {
        if(fullName == null) throw new AppDaoException("Ошибка! Не указан имя для поиска.");

        final Map<String, String> parameters = Map.of("firstName", fullName.getFirstName()
                                                        ,"lastName", fullName.getLastName()
                                                        ,"familyName", fullName.getFamilyName());
        final String sql = "SELECT a.id, a.first_name, a.last_name, a.family_name FROM AUTHOR a " +
                "WHERE upper(a.first_name) = upper(:firstName) " +
                "AND upper(a.last_name) = upper(:lastName) " +
                "AND upper(a.family_name) = upper(:familyName)";
        Optional<Author> authorOpt;
        try {
            authorOpt = Optional.ofNullable(namedParameterJdbcOperations.queryForObject(sql, parameters, new AuthorDaoJdbc.AuthorMapper()));
        } catch (Exception e) {
            authorOpt = Optional.empty();
        }
        return authorOpt;
    }


    @Getter
    static final class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String familyName = rs.getString("family_name");

            return new Author(id, firstName, lastName, familyName);
        }
    }
}
