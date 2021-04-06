package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.exception.DaoException;
import by.leonovich.hibernatecrm.mappings.singletable.Employee;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

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
@Repository
public class EmployeeDao extends BaseDao<Employee> {

    public Employee getHighestPaidEmployee() throws DaoException {
        try {
            Session session = session();

            CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> cQuery = cBuilder.createQuery(Employee.class);
            Root<Employee> rootEntry = cQuery.from(Employee.class);
            cQuery.orderBy(cBuilder.desc(rootEntry.get("salary")));

            cQuery.select(rootEntry);
            Query<Employee> query = session.createQuery(cQuery);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }

    public List<Employee> getEmployeesOrderedBySalary() throws DaoException {
        try {
            Session session = session();

            CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> cQuery = cBuilder.createQuery(Employee.class);
            Root<Employee> rootEntry = cQuery.from(Employee.class);
            cQuery.orderBy(cBuilder.asc(rootEntry.get("salary")));

            cQuery.select(rootEntry);
            Query<Employee> query = session.createQuery(cQuery);
            return query.getResultList();
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
    }
}
