package by.leonovich.hibernatecrm.service.person;

import by.leonovich.hibernatecrm.common.annotation.LogExecutionTime;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.exception.DaoException;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * Created : 12/03/2021 19:19
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService<Person> {

    private Dao<Person> personDao;

    @LogExecutionTime
    @Override
    public Person create(Person obj) throws ServiceException {
        try {
            Thread.sleep(RandomNumber.DAYS.get());
            return personDao.get(personDao.save(obj));
        } catch (DaoException | InterruptedException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Person read(Serializable id) throws ServiceException {
        try {
            return personDao.get(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Collection<Person> read() throws ServiceException {
        try {
            return personDao.getAll(Person.class);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Person update(Person obj) throws ServiceException {
        try {
            personDao.saveOrUpdate(obj);
            return personDao.get(obj.getId());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Person obj) throws ServiceException {
        try {
            personDao.delete(obj);
            return Objects.isNull(personDao.get(obj.getId()));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Autowired
    public void setPersonDao(Dao<Person> personDao) {
        this.personDao = personDao;
    }
}
