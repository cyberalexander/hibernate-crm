package by.leonovich.hibernatecrm.service.person;

import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.service.exception.ServiceException;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created : 12/03/2021 19:14
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public interface PersonService<T extends Person> {

    T create(T obj) throws ServiceException;

    T read(Serializable id) throws ServiceException;

    Collection<T> read() throws ServiceException;

    T update(T obj) throws ServiceException;

    boolean delete(T obj) throws ServiceException;
}
