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
import java.util.Optional;

/**
 * Created : 26/11/2020 22:01
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public abstract class BaseDao<T> implements Dao<T> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);
    private Transaction transaction = null;

    public void saveOrUpdate(T t) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(t);
            LOG.info("saveOrUpdate(t): {}", t);
            transaction.commit();
            session.evict(t);
            LOG.info("Save or update (commit): {}", t);
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    public T get(Serializable id) throws DaoException {
        LOG.info("Get class by id:" + id);
        T t = null;
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            transaction = session.beginTransaction();
            t = (T) session.get(getPersistentClass(), id);
            transaction.commit();
            Optional.ofNullable(t).ifPresent(session::evict);
            LOG.info("get clazz:" + t);
        } catch (HibernateException e) {
            transaction.rollback();
            LOG.error("Error get " + getPersistentClass() + " in Dao" + e);
            throw new DaoException(e);
        }
        return t;
    }

    public T load(Serializable id) throws DaoException {
        LOG.info("Load class by id:" + id);
        T t = null;
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            transaction = session.beginTransaction();
            t = (T) session.load(getPersistentClass(), id);
            LOG.info("load() clazz:" + t);
            session.isDirty();
            transaction.commit();
        } catch (HibernateException e) {
            LOG.error("Error load() " + getPersistentClass() + " in Dao" + e);
            transaction.rollback();
            throw new DaoException(e);
        }
        return t;
    }

    public void delete(T t) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            transaction = session.beginTransaction();
            session.delete(t);
            transaction.commit();
            LOG.info("Delete:" + t);
        } catch (HibernateException e) {
            LOG.error("Error during " + t + " removing.", e);
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    @Override
    public List<T> getAll(Class<T> type) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
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
            Session session = HibernateUtil.getHibernateUtil().getSession();
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
        return null;
    }

    @SuppressWarnings("unchecked")
    private Class getPersistentClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
