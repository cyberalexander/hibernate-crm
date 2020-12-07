package by.leonovich.hibernatecrm.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created : 26/11/2020 22:00
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public interface Dao<T> {

    /**
     * Persist entity in database
     * @param entity Object to be persisted in database
     * @return Generated unique ID
     */
    Serializable save(T entity) throws DaoException;

    void saveOrUpdate(T entity) throws DaoException;

    T get(Serializable id) throws DaoException;

    T load(Serializable id) throws DaoException;

    void delete(T entity) throws DaoException;

    List<T> getAll(Class<T> type) throws DaoException;

    List<Serializable> getIds() throws DaoException;

    Serializable getLastIndex() throws DaoException;
}
