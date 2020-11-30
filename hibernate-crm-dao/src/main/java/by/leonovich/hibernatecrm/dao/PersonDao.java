package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created : 26/11/2020 22:07
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class PersonDao extends BaseDao<Person> {

    /**
     * Explicitly flushing information from context to database. If entity was changed in context,
     * changes will be stored in database.
     */
    public void sessionFlushDemo(Long id, String newName) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            transaction = session.beginTransaction();
            Person p = session.get(Person.class, id);
            LOG.info("Dirty={}. Received from Session name = {}", session.isDirty(), p.getName());
            p.setName(newName);
            LOG.info("Dirty={}. After modification in application name = {}", session.isDirty(), p.getName());
            session.flush();
            LOG.info("Dirty={}. After FLUSH : {}", session.isDirty(), p);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    /**
     * Refresh information about entity(ies) in context with actual data from database. If entity was changed
     * in context, refresh will erase those changes.
     */
    public void sessionRefreshDemo(Long id, String newName) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            transaction = session.beginTransaction();
            Person p = session.get(Person.class, id);
            LOG.info("Dirty={}. Received from Session name = {}", session.isDirty(), p.getName());
            p.setName(newName);
            LOG.info("Dirty={}. After modification in application name = {}", session.isDirty(), p.getName());
            session.refresh(p);
            LOG.info("Dirty={}. After REFRESH : {}", session.isDirty(), p);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }
}
