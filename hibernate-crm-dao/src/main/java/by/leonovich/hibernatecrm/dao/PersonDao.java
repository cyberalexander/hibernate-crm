package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.mappings.singletable.Person;
import org.springframework.stereotype.Repository;

/**
 * DAO pattern implementation specific to {@link Person} entity
 * Created : 26/11/2020 22:07
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Repository
public class PersonDao extends BaseDao<Person> {
}
