package ru.zav.storedbooksinfo.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public BookRepositoryJpa(EntityManager entityManager) {
        this.em = entityManager;
    }

    /**Получение Book по ID
     * @return Объект Book*/
    @Override
    public Optional<Book> getById(String id) {
        try {
            return Optional.ofNullable(em.find(Book.class, id));
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }

    /**Удаление книги по ID
     * @return количество удаленных строк*/
    @Transactional
    @Override
    public int deleteById(String id) {
        try {
            final Optional<Book> optionalBook = getById(id);
            if(optionalBook.isPresent()) {
                em.remove(optionalBook.get());
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось удалить объект с ID: %s. Причина: %s", id,  e.getCause()), e);
        }
    }

    /**Сохранение Книги.
     * @return Актуальный объект*/
    @Transactional
    @Override
    public Book save(Book book) {
        if(book.getId() == null){
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    /**Получение всего содержимого таблицы*/
    @Override
    public List<Book> readAll() {
        try {
            return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }

    /**Очистка таблицы*/
    @Transactional
    @Override
    public void clearAll() {
        try {
            em.createQuery("TRUNCATE TABLE BOOK").executeUpdate();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось сбросить таблицу. Причина: %s", e.getCause()), e);
        }
    }


    @Transactional
    @Override
    public List<Book> findByGenre(Genre genre) {
        if(genre == null) return new ArrayList<>();

        final String sql = "SELECT b FROM Book b WHERE b.genre.id = :genreId";
        try {
            return em.createQuery(sql, Book.class).setParameter("genreId", genre.getId()).getResultList();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }

    @Override
    public List<Book> findByTitle(String title) {
        try {
            final String sql = "SELECT b FROM Book b WHERE UPPER(b.title) LIKE '%'||UPPER(:title)||'%'";
            return em.createQuery(sql, Book.class).setParameter("title", StringUtils.trim(title)).getResultList();
        } catch (Exception e) {
            throw new AppDaoException(String.format("Не удалось получить объект. Причина: %s", e.getCause()), e);
        }
    }
}
