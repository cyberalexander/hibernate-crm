package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.exception.DaoException;
import by.leonovich.hibernatecrm.mappings.singletable.Employee;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created : 02/01/2021 18:54
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class EmployeeDao extends BaseDao<Employee> {

    public Employee getHighestPaidEmployee() throws DaoException {
        try {
            Session session = hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> cQuery = cBuilder.createQuery(Employee.class);
            Root<Employee> rootEntry = cQuery.from(Employee.class);
            cQuery.orderBy(cBuilder.desc(rootEntry.get("salary")));

            cQuery.select(rootEntry);
            Query<Employee> query = session.createQuery(cQuery);
            query.setMaxResults(1);
            Employee result = query.getSingleResult();
            transaction.commit();
            return result;
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }

    public List<Employee> getEmployeesOrderedBySalary() throws DaoException {
        try {
            Session session = hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> cQuery = cBuilder.createQuery(Employee.class);
            Root<Employee> rootEntry = cQuery.from(Employee.class);
            cQuery.orderBy(cBuilder.asc(rootEntry.get("salary")));

            cQuery.select(rootEntry);
            Query<Employee> query = session.createQuery(cQuery);
            List<Employee> result = query.getResultList();
            transaction.commit();
            return result;
        } catch (HibernateException e) {
            transaction.rollback();
            throw new DaoException(e);
        }
    }
}
