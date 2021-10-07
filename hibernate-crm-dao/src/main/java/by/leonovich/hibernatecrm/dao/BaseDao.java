package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created : 26/11/2020 22:01
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public abstract class BaseDao<T> implements Dao<T> {
    protected static final Logger log = LogManager.getLogger(BaseDao.class);
    private SessionFactory hibernate;

    @Override
    public void persist(T entity) throws DaoException {
        try {
            log.debug("Before persist {}", entity);
            session().persist(entity);
            log.debug("After persist {}", entity);
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Serializable save(T entity) throws DaoException {
        try {
            log.debug("Before save {}", entity);
            Serializable id = session().save(entity);
            log.debug("After save {}", entity);
            return id;
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void saveOrUpdate(T entity) throws DaoException {
        try {
            log.debug("Before {}", entity);
            session().saveOrUpdate(entity);
            log.debug("After {}", entity);
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public T get(Serializable id) throws DaoException {
        try {
            return session().get(getPersistentClass(), id);
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public T load(Serializable id) throws DaoException {
        try {
            return session().load(getPersistentClass(), id);
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(T entity) throws DaoException {
        try {
            session().delete(entity);
            log.debug("Deleted : {}", entity);
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<T> getAll(Class<T> type) throws DaoException {
        try {
            Session session = session();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);
            Root<T> rootEntry = query.from(type);
            query.select(rootEntry);
            Query<T> allQuery = session.createQuery(query);
            return allQuery.getResultList();
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public MagicList<Serializable> getIds() throws DaoException {
        try {
            Session session = session();
            Class clazz = getPersistentClass();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Serializable> query = builder.createQuery(clazz);
            Root<Serializable> rootEntry = query.from(clazz);
            query.select(rootEntry.get("id"));
            Query<Serializable> allQuery = session.createQuery(query);
            return new MagicList<>(allQuery.getResultList());
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Serializable getLastIndex() throws DaoException {
        try {
            String hql = "SELECT MAX(id) FROM " + getPersistentClass().getSimpleName();
            return (Serializable) session().createQuery(hql).list().get(0);
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getPersistentClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Session session() {
        return hibernate.getCurrentSession();
    }

    @Autowired
    public void setHibernate(SessionFactory hibernate) {
        this.hibernate = hibernate;
    }
}
