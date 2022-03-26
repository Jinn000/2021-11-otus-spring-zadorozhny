package ru.zav.storedbooksinfo.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.dao.datatypes.EntityId;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class GenreRepositoryJpa implements GenreRepository {
    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public GenreRepositoryJpa(EntityManager entityManager) {
        this.em = entityManager;
    }

    /**Получение Genre по ID
     * @return Объект Genre*/
    @Override
    public Genre getById(EntityId id) {
        try {
            return em.createQuery("SELECT g FROM Genre g WHERE g.id = :id", Genre.class).setParameter("id", id.getIdValue()).getSingleResult();
        }catch (NoResultException emptyEx) {
            return null;
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }

    /**Удаление по ID
     * @return количество удаленных строк*/
    @Override
    public int deleteById(EntityId id) {
        try {
            final Query query = em.createQuery("DELETE FROM Genre g WHERE g.id = :id");
            query.setParameter("id", id.getIdValue());
            return query.executeUpdate();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось удалить объект с ID: %s. Причина: %s", id,  e.getCause()), e);
        }
    }

    /**Сохранение обьекта.
     * @return сохраненный объект Жанр*/
    @Override
    public Genre save(Genre genre) {
        if(genre.getId() == null){
            em.persist(genre);
            return genre;
        } else {
            return em.merge(genre);
        }
    }

    /**Получение всего содержимого таблицы*/
    @Override
    public List<Genre> readAll() {
        try {
            return em.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }

    /**Очистка таблицы*/
    @Override
    public void clearAll() {
        try {
            em.createQuery("TRUNCATE TABLE Genre").executeUpdate();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось сбросить таблицу. Причина: %s", e.getCause()), e);
        }
    }

    @Override
    public Optional<Genre> findByDescription(String description) {
        if(description == null) throw new AppDaoException("Ошибка! Не указан Description жанра для поиска.");

        try {
            final TypedQuery<Genre> genreTypedQuery = em.createQuery("SELECT g FROM Genre g WHERE upper(g.description) = :description", Genre.class);
            genreTypedQuery.setParameter("description", StringUtils.upperCase(StringUtils.trim(description)));
            return Optional.ofNullable(genreTypedQuery.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
