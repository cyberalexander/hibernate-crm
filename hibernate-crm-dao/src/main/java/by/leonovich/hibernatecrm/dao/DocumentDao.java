package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.exception.DaoException;
import by.leonovich.hibernatecrm.mappings.joinedtable.Document;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

/**
 * Created : 10/12/2020 10:12
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class DocumentDao extends BaseDao<Document> {

    public List<Document> getExpiringThisYearDocumentsCriteria() throws DaoException {
        try {
            Session session = hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Document> cQuery = cBuilder.createQuery(Document.class);
            Root<Document> rootEntry = cQuery.from(Document.class);
            Predicate[] predicates = {
                cBuilder.equal(
                    cBuilder.function("year", Integer.class, rootEntry.get("expirationDate")),
                    LocalDate.now().getYear()
                )};
            cQuery.where(predicates);

            cQuery.select(rootEntry);
            Query<Document> query = session.createQuery(cQuery);
            List<Document> result = query.getResultList();
            transaction.commit();
            return result;
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    public List<Document> getExpiringThisYearDocumentsHql() throws DaoException {
        try {
            Session session = hibernate.getSession();
            transaction = session.beginTransaction();
            String hql = "FROM " + Document.class.getSimpleName() + " WHERE YEAR(expirationDate) <= :inputDate";
            Query<Document> query = session.createQuery(hql);
            query.setParameter("inputDate", LocalDate.now().getYear());
            List<Document> result = query.list();
            transaction.commit();
            return result;
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }
}
