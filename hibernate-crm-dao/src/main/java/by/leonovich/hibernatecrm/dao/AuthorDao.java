package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.mappings.annotation.Author;
import by.leonovich.hibernatecrm.exception.DaoException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created : 07/01/2021 11:19
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class AuthorDao extends BaseDao<Author> {

    public List<Author> getAuthorByNameCriteria(String name) throws DaoException {
        try {
            Session session = hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Author> cQuery = cBuilder.createQuery(Author.class);
            Root<Author> rootEntry = cQuery.from(Author.class);
            Predicate[] predicates = {cBuilder.equal(rootEntry.get("name"), name)};
            cQuery.where(predicates);

            cQuery.select(rootEntry);
            Query<Author> query = session.createQuery(cQuery);
            List<Author> authors = query.getResultList();
            transaction.commit();
            return authors;
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    public List<Author> getAuthorByNameHql(String name) throws DaoException {
        try {
            Session session = hibernate.getSession();
            transaction = session.beginTransaction();
            String hql = "FROM " + Author.class.getSimpleName() + " WHERE name=:name";
            Query<Author> query = session.createQuery(hql);
            query.setParameter("name", name);
            List<Author> authors = query.list();
            transaction.commit();
            return authors;
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }
}
