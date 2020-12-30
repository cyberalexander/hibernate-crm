package by.leonovich.hibernatecrm.dao.person;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.singletable.Address;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.mappings.singletable.PhoneNumber;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created : 27/11/2020 09:54
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class PersonDaoTest extends CommonPersonDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(PersonDaoTest.class);

    @Test
    @SneakyThrows
    void testPersist() {
        Person person = Person.init();
        dao.persist(person);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_PERSIST, person),
            dao.get(person.getId()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSave() {
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            dao.save(Person.init()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_Save() {
        Person toSave = Person.init();
        dao.saveOrUpdate(toSave);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE, toSave),
            dao.get(toSave.getId()),
            Matchers.equalTo(toSave)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_Update() {
        Person toUpdate = allPersons.randomEntity();
        dao.saveOrUpdate(toUpdate.modify());
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, toUpdate),
            dao.get(toUpdate.getId()),
            Matchers.equalTo(toUpdate)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Person newPerson = Person.init();
        dao.save(newPerson);
        PhoneNumber modifiedCascade = newPerson.modifyCascade().getPhoneNumber();
        dao.saveOrUpdate(newPerson);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, modifiedCascade),
            dao.get(newPerson.getId()).getPhoneNumber(),
            Matchers.equalTo(modifiedCascade)
        );
    }

    @Test
    @SneakyThrows
    void testGet() {
        Person person = allPersons.randomEntity();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, person.getClass().getSimpleName(), person.getId()),
            dao.get(person.getId()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testGetWhenNotExists() {
        Serializable index = allPersons.lastElement().getId() + 300L;
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET_NOT_EXISTS, index),
            dao.get(index),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testLoad() {
        Serializable randomIndex = allPersons.randomEntity().getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_LOAD, Person.class.getSimpleName(), randomIndex),
            dao.load(randomIndex),
            Matchers.notNullValue()
        );
    }

    /**
     * Method {@link Session#load} is different from {@link Session#get}
     * In case of "get", when object not exists in database, hibernate returns null. When in case of "load"
     * hibernate will not query object immediately but will just return proxy. And when we try to access any property
     * of that proxy, hibernate will immediately execute select to database and throw {@link ObjectNotFoundException}
     * if object won't be found there.
     */
    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testLoadWhenNotExists() {
        Serializable index = allPersons.lastElement().getId() + 300L;
        Assertions.assertThrows(
            ObjectNotFoundException.class,
            () -> dao.load(index).getName(),
            TestConstants.M_LOAD_EXCEPTION);
    }

    @Test
    @SneakyThrows
    void testDelete() {
        Person toDelete = Person.init();

        dao.saveOrUpdate(toDelete); /* 1. Save object in DB */
        Assertions.assertNotNull(toDelete.getId(), TestConstants.M_SAVE);
        dao.delete(toDelete); /* 2. Delete object from DB */

        MatcherAssert.assertThat(String.format(TestConstants.M_DELETE, toDelete),
            dao.get(toDelete.getId()), /* 3. Try to get deleted object from DB */
            Matchers.nullValue()
        );
    }

    /**
     * Comparing IDs here, because other information might be changed at the time other tests are running,
     * like {@link PersonDaoTest#testSaveOrUpdate_Update()}
     */
    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testGetAll() {
        List<Serializable> queried = dao.getAll(Person.class).stream()
            .map(Person::getId)
            .collect(Collectors.toList());
        allPersons.stream().map(Person::getId).forEach(p ->
            MatcherAssert.assertThat(
                String.format(TestConstants.M_GET_ALL, Person.class.getSimpleName(), p),
                queried.contains(p),
                Matchers.is(Boolean.TRUE)
            ));
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testGetIds() {
        List<Serializable> queried = dao.getIds();
        allPersons.stream().map(Person::getId).forEach(id -> {
            MatcherAssert.assertThat(
                String.format(TestConstants.M_GET_IDS, id),
                queried.contains(id),
                Matchers.is(Boolean.TRUE)
            );
        });
    }

    /**
     * Test {@link Address#getIndex()} "hibernate-calculable" field
     */
    @Test
    @SneakyThrows
    void testPersonAddressColumnIndex() {
        Address address = dao.get(allPersons.randomEntity().getId()).getHomeAddress();
        String expected = address.getCountryCode() + "_" + address.getCountry();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_TEST_INDEX, expected),
            address.getIndex(),
            Matchers.equalTo(expected)
        );
    }
}
