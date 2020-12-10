package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
class PersonDaoTest extends AbstractPersonDaoTest {

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
    void testSaveOrUpdateSave() {
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
    void testSaveOrUpdateUpdate() {
        Person toUpdate = allPersons.get(randIndex()).populate();
        toUpdate.setName("UPDATE_" + RandomString.NAME.get() + "_" + toUpdate.getId());
        dao.saveOrUpdate(toUpdate);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, toUpdate),
            dao.get(toUpdate.getId()),
            Matchers.equalTo(toUpdate)
        );
    }

    @Test
    @SneakyThrows
    void testGetPerson() {
        Serializable randomIndex = allPersons.get(randIndex()).getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, Person.class.getSimpleName(), randomIndex),
            dao.get(randomIndex),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testGetPersonNotExists() {
        Serializable index = allPersons.get(allPersons.size() - 1).getId() + 300L;
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET_NOT_EXISTS, index),
            dao.get(index),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testLoadPerson() {
        Serializable randomIndex = allPersons.get(randIndex()).getId();
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
    void testLoadPersonNotExists() {
        Serializable index = allPersons.get(allPersons.size() - 1).getId() + 300L;
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
     * like {@link PersonDaoTest#testSaveOrUpdateUpdate()}
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
}
