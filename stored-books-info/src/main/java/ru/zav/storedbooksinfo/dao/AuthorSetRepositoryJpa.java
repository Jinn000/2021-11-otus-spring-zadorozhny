package ru.zav.storedbooksinfo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.domain.AuthorSet;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Repository
public class AuthorSetRepositoryJpa implements AuthorSetRepository {

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public AuthorSetRepositoryJpa(EntityManager entityManager) {
        this.em = entityManager;
    }

    /**Получение AuthorSet по ID
     * @return Объект AuthorSet*/
    @Override
    public AuthorSet getById(String id) {
        try {
            return em.find(AuthorSet.class, id);
        } catch (EmptyResultDataAccessException emptyEx) {
            return null;
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }
    /**Получение коллекции AuthorSet по ID автора
     * @return Объект AuthorSet*/
    @Override
    public List<AuthorSet> findByAuthorId(String authorId) {
        try {
            final String sql = "SELECT s FROM AuthorSet s WHERE s.author.id = :authorId";
            final TypedQuery<AuthorSet> typedQuery = em.createQuery(sql, AuthorSet.class);
            typedQuery.setParameter("authorId", authorId);
            return typedQuery.getResultList();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }

    /**Получение коллекции AuthorSet по ID автора
     * @return Объект AuthorSet*/
    @Override
    public List<AuthorSet> findByAuthorsSetId(String authorsSetId) {
        try {
            final String sql = "SELECT s FROM AuthorSet s WHERE s.authorsSetId = :authorsSetId";
            final TypedQuery<AuthorSet> typedQuery = em.createQuery(sql, AuthorSet.class);
            typedQuery.setParameter("authorsSetId", authorsSetId);
            return typedQuery.getResultList();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }

    /**Удаление по ID
     * @return количество удаленных строк*/
    @Override
    public int deleteById(String id) {
        try {
            return em.createQuery("DELETE FROM AuthorSet a WHERE a.id = :id").setParameter("id", id).executeUpdate();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось удалить объект с ID: %s. Причина: %s", id,  e.getCause()), e);
        }
    }

    /**Сохранение обьекта.
     * @return объект AuthorSet*/
    @Override
    public AuthorSet save(AuthorSet authorSet) {
        if(authorSet.getAuthorsSetId() == null){
            em.persist(authorSet);
            return authorSet;
        } else {
            return em.merge(authorSet);
        }
    }

    /**Получение всего содержимого таблицы*/
    @Override
    public List<AuthorSet> readAll() {
        try {
            return em.createQuery("SELECT s FROM AuthorSet s", AuthorSet.class).getResultList();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }

    /**Очистка таблицы*/
    @Override
    public void clearAll() {
        try {
            em.createQuery("TRUNCATE TABLE AuthorSet").executeUpdate();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось сбросить таблицу. Причина: %s", e.getCause()), e);
        }
    }
}
