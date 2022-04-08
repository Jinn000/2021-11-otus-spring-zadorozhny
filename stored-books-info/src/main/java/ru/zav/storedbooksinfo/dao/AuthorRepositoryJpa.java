package ru.zav.storedbooksinfo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.datatypes.FullName;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;


@Repository
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public AuthorRepositoryJpa(EntityManager entityManager) {
        this.em = entityManager;
    }

    /**Получение Author по ID
     * @return Объект Author*/
    @Override
    public Author getById(String id) {
        Author author;
        try {
            author = em.find(Author.class, id);
        } catch (EmptyResultDataAccessException emptyEx) {
            author = null;
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return author;
    }

    /**Удаление по ID
     * @return количество удаленных строк*/
    @Transactional
    @Override
    public int deleteById(String id) {
        try {
            final Author author = getById(id);
            em.remove(author);
            return 1;
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось удалить объект с ID: %s. Причина: %s", id,  e.getCause()), e);
        }
    }

    /**Сохранение обьекта.
     * @return обновленный обьект*/
    @Transactional
    @Override
    public Author save(Author author){
        if(author.getId() == null){
            em.persist(author);
            return author;
        }else{
            return em.merge(author);
        }
    }
    /**Получение всего содержимого таблицы*/
    @Override
    public List<Author> readAll() {
        try {
            return em.createQuery("SELECT a FROM Author a", Author.class).getResultList();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }

    /**Очистка таблицы*/
    @Transactional
    @Override
    public void clearAll() {
        try {
            em.createQuery("TRUNCATE TABLE AUTHOR").executeUpdate();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось сбросить таблицу. Причина: %s", e.getCause()), e);
        }
    }

    @Override
    public Optional<Author> findByFullName(FullName fullName) {
        if(fullName == null) throw new AppDaoException("Ошибка! Не указан имя для поиска.");

        final String sql = "SELECT a FROM Author a " +
                "WHERE upper(a.firstName) = upper(:firstName) " +
                "AND upper(a.lastName) = upper(:lastName) " +
                "AND upper(a.familyName) = upper(:familyName)";
        Optional<Author> authorOpt;
        try {
            authorOpt = Optional.ofNullable(em.createQuery(sql, Author.class)
                    .setParameter("firstName", fullName.getFirstName())
                    .setParameter("lastName", fullName.getLastName())
                    .setParameter("familyName", fullName.getFamilyName())
                    .getSingleResult());
        } catch (Exception e) {
            authorOpt = Optional.empty();
        }
        return authorOpt;
    }
}
