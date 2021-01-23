package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    protected static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);
    protected Transaction transaction;
    protected HibernateUtil hibernate;

    public BaseDao() {
        hibernate = HibernateUtil.getInstance();
    }

    @Override
    public void persist(T entity) throws DaoException {
        try {
            Session session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();
            LOG.debug("Before persist {}", entity);
            session.persist(entity);
            LOG.debug("After persist {}", entity);
            transaction.commit();
            session.evict(entity);
            session.clear();
            LOG.debug("After commit {}", entity);
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    @Override
    public Serializable save(T entity) throws DaoException {
        try {
            Session session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();
            LOG.debug("Before save {}", entity);
            Serializable id = session.save(entity);
            LOG.debug("After save {}", entity);
            transaction.commit();
            session.evict(entity);
            session.clear();
            LOG.debug("After commit {}", entity);
            return id;
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    @Override
    public void saveOrUpdate(T entity) throws DaoException {
        try {
            Session session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();
            LOG.debug("Before {}", entity);
            session.saveOrUpdate(entity);
            LOG.debug("After {}", entity);
            transaction.commit();
            session.evict(entity);
            session.clear();
            LOG.debug("After commit {}", entity);
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    @Override
    public T get(Serializable id) throws DaoException {
        try {
            Session session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();
            T t = session.get(getPersistentClass(), id);
            transaction.commit();
            LOG.debug("Request : {}; Result : {}", id, t);
            return t;
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    @Override
    public T load(Serializable id) throws DaoException {
        try {
            Session session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();
            T t = session.load(getPersistentClass(), id);
            LOG.debug("Session#isDirty={}; Request : {}; Result : {}", session.isDirty(), id, t);
            transaction.commit();
            return t;
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(T entity) throws DaoException {
        try {
            Session session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
            LOG.debug("Deleted : {}", entity);
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    @Override
    public List<T> getAll(Class<T> type) throws DaoException {
        try {
            Session session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);
            Root<T> rootEntry = query.from(type);
            query.select(rootEntry);
            Query<T> allQuery = session.createQuery(query);
            List<T> result = allQuery.getResultList();
            transaction.commit();
            return result;
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    @Override
    public List<Serializable> getIds() throws DaoException {
        try {
            Session session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();

            Class clazz = getPersistentClass();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Serializable> query = builder.createQuery(clazz);
            Root<Serializable> rootEntry = query.from(clazz);
            query.select(rootEntry.get("id"));
            Query<Serializable> allQuery = session.createQuery(query);
            List<Serializable> result = allQuery.getResultList();
            transaction.commit();
            return result;
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    @Override
    public Serializable getLastIndex() throws DaoException {
        try {
            String hql = "SELECT MAX(id) FROM " + getPersistentClass().getSimpleName();
            Session session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery(hql);
            List<Serializable> list = query.list();

            transaction.commit();
            return list.get(0);

        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<T> getPersistentClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
