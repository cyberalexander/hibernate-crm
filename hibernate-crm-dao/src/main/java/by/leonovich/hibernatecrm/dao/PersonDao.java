package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created : 26/11/2020 22:07
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class PersonDao extends BaseDao<Person> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);

    public void sessionFlushDemo(Integer id, String newName) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            Person p = session.get(Person.class, id);
            System.out.println("Received from Session : " + p);
            p.setName(newName);
            System.out.println("After modification in application : " + p);
            session.flush();
            System.out.println("After FLUSH : " + p);
        } catch (HibernateException e) {
            throw new DaoException(e);
        }

    }

    public void sessionRefreshDemo(Integer id, String newName) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            Person p = (Person)session.get(Person.class, id);
            System.out.println("Received from Session : " + p);
            p.setName(newName);
            System.out.println("After modification in application : " + p);
            session.refresh(p);
            System.out.println("After REFRESH : " + p);
        } catch (HibernateException e) {
            throw new DaoException(e);
        }

    }
}
