package ru.zav.storedbooksinfo.dao;

import lombok.Getter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.zav.storedbooksinfo.dao.datatypes.EntityId;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GenreDaoJdbc implements GenreDao{
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public GenreDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    /**Получение Genre по ID
     * @return Объект Genre*/
    @Override
    public Genre getById(EntityId id) throws AppDaoException {
        final Map<String, String> parameters = Map.of("id", id.getIdValue());
        final String sql = "SELECT g.id, g.description FROM GENRE g WHERE g.id = :id";
        final Genre genre;
        try {
            genre = namedParameterJdbcOperations.queryForObject(sql, parameters, new GenreMapper());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return genre;
    }

    /**Удаление по ID
     * @return количество удаленных строк*/
    @Override
    public int deleteById(EntityId id) throws AppDaoException {
        final Map<String, String> parameters = Map.of("id", id.getIdValue());
        final String sql = "DELETE FROM GENRE g WHERE g.id = :id";

        try {
            return namedParameterJdbcOperations.update(sql, parameters);
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось удалить объект с ID: %s. Причина: %s", id,  e.getCause()), e);
        }
    }

    /**Вставка обьекта.
     * @return количество добавленных строк*/
    @Override
    public int insert(Genre genre) throws AppDaoException {
        if(genre.getDescription() == null) throw new AppDaoException("Ошибка! Не указан Description для добавляемого жанра.");

        final boolean isDuplicate = readAll().stream()
                .map(Genre::getDescription)
                .anyMatch(item -> item.equals(genre.getDescription()));

        if(isDuplicate) return 0; // дубли очевидно не нужны

        final Map<String, String> parameters = Map.of("id", genre.getId(), "description", genre.getDescription());
        final String sql = "INSERT INTO GENRE (id, description) values (:id, :description)";

        try {
            return namedParameterJdbcOperations.update(sql, parameters);
        } catch (Exception e) {
            throw new AppDaoException(String.format("Вставка обьекта не удалась. Причина: %s", e.getCause()), e);
        }
    }

    /**Получение всего содержимого таблицы*/
    @NotNull
    @Override
    public List<Genre> readAll() throws AppDaoException {
        final String sql = "SELECT g.id, g.description FROM GENRE g";
        final List<Genre> genreList;
        try {
            genreList = namedParameterJdbcOperations.query(sql, new GenreMapper());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return genreList;
    }

    /**Очистка таблицы*/
    @Override
    public void clearAll() throws AppDaoException {
        final String sql = "TRUNCATE TABLE GENRE";
        try {
            namedParameterJdbcOperations.update(sql, Map.of());
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось сбросить таблицу. Причина: %s", e.getCause()), e);
        }
    }

    @Override
    public Optional<Genre> findByDescription(String description) throws AppDaoException {
        if(description == null) throw new AppDaoException("Ошибка! Не указан Description жанра для поиска.");

        final Map<String, String> parameters = Map.of("description", description);
        final String sql = "SELECT g.id, g.description FROM GENRE g WHERE g.description = :description";
        Optional<Genre> genreOpt;
        try {
            genreOpt = Optional.ofNullable(namedParameterJdbcOperations.queryForObject(sql, parameters, new GenreMapper()));
        } catch (Exception e) {
            genreOpt = Optional.empty();
        }
        return genreOpt;
    }


    @Getter
    static final class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("id");
            String description = rs.getString("description");

            return new Genre(id, description);
        }
    }
}
