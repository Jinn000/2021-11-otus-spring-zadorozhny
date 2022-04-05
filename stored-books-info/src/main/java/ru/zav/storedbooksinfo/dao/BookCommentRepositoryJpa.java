package ru.zav.storedbooksinfo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.domain.BookComment;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookCommentRepositoryJpa implements BookCommentRepository{

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public BookCommentRepositoryJpa(EntityManager entityManager) {
        this.em = entityManager;
    }

    /**Получение BookComment по ID
     * @return Объект BookComment*/
    @Override
    @Transactional(readOnly = true)
    public BookComment getById(String id) {
        BookComment bookComment;
        try {
            bookComment = em.find(BookComment.class, id);
        } catch (EmptyResultDataAccessException emptyEx) {
            bookComment = null;
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
        return bookComment;
    }

    /**Удаление по ID
     * @return количество удаленных строк*/
    @Transactional
    @Override
    public int deleteById(String id) {
        final Query query = em.createQuery("DELETE FROM BookComment b WHERE b.id = :id");
        query.setParameter("id", id);

        try {
            return query.executeUpdate();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось удалить объект с ID: %s. Причина: %s", id,  e.getCause()), e);
        }
    }

    /**Сохранение обьекта.
     * @return обновленный объект*/
    @Transactional
    @Override
    public BookComment save(BookComment bookComment){
        if(bookComment.getId() == null){
            em.persist(bookComment);
            return bookComment;
        }else{
            return em.merge(bookComment);
        }
    }
    /**Получение всего содержимого таблицы*/
    @Transactional(readOnly = true)
    @Override
    public List<BookComment> readAll() {
        try {
            return em.createQuery("SELECT b FROM BookComment b", BookComment.class).getResultList();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }

    /**Очистка таблицы*/
    @Transactional
    @Override
    public void clearAll() {
        try {
            em.createQuery("TRUNCATE TABLE BOOK_COMMENT").executeUpdate();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось сбросить таблицу. Причина: %s", e.getCause()), e);
        }
    }

    /**Поиск комментариев к книге, по ее ID*/
    @Transactional(readOnly = true)
    @Override
    public List<BookComment> findByBookId(String bookId) {
        if(bookId == null) throw new AppDaoException("Ошибка! Не указан bookId для поиска.");

        final String sql = "SELECT b FROM BookComment b " +
                "WHERE b.bookId = upper(:bookId)";
        List<BookComment> bookCommentList;
        try {
            return em.createQuery(sql, BookComment.class)
                    .setParameter("bookId", bookId)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    //*****************************************************

}
