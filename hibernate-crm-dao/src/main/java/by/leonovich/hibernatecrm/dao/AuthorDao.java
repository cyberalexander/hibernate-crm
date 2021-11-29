package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.exception.DaoException;
import by.leonovich.hibernatecrm.mappings.annotation.Author;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

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
@Repository
public class AuthorDao extends BaseDao<Author> {

    public List<Author> getAuthorByNameCriteria(final String name) throws DaoException {
        try {
            Session session = session();

            CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Author> cQuery = cBuilder.createQuery(Author.class);
            Root<Author> rootEntry = cQuery.from(Author.class);
            Predicate[] predicates = {cBuilder.equal(rootEntry.get("name"), name)};
            cQuery.where(predicates);

            cQuery.select(rootEntry);
            Query<Author> query = session.createQuery(cQuery);
            return query.getResultList();
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }

    public List<Author> getAuthorByNameHql(final String name) throws DaoException {
        try {
            String hql = "FROM " + Author.class.getSimpleName() + " WHERE name=:name";
            Query<Author> query = session().createQuery(hql);
            query.setParameter("name", name);
            return query.list();
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }
}
