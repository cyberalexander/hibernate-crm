package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.tools.RandomStrings;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.leonovich.hibernatecrm.tools.RandomStrings.NAME;
import static by.leonovich.hibernatecrm.tools.RandomStrings.SURNAME;

/**
 * Created : 27/11/2020 09:54
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class PersonDaoTest {
    private static final Logger LOG = LoggerFactory.getLogger(PersonDaoTest.class);

    private static final PersonDao personDao = new PersonDao();
    private static List<Person> all;
    private static Random r;

    @BeforeAll
    static void beforeAll() {
        r = new Random();

        all = Stream.generate(Person::new)
            .limit(10)
            .map(person -> {
                person.setName(new RandomStrings(NAME).get());
                person.setSurname(new RandomStrings(SURNAME).get());
                person.setAge(new Random().nextInt(99 - 1) + 1);
                try {
                    personDao.saveOrUpdate(person);
                } catch (DaoException e) {
                    throw new RuntimeException(e);
                }
                return person;
            })
            .collect(Collectors.toList());
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate() {
        Person p = new Person();
        p.setName(new RandomStrings(NAME).get());
        p.setSurname(new RandomStrings(SURNAME).get());
        p.setAge(new Random().nextInt(99 - 1) + 1);

        personDao.saveOrUpdate(p);

        MatcherAssert.assertThat(String.format("It was expected loaded from db object to be equal to %s", p),
            personDao.get(p.getId()),
            Matchers.equalTo(p)
        );
    }

    @Test
    @SneakyThrows
    void testGetPerson() {
        Serializable randomIndex = all.get(r.nextInt(all.size())).getId();
        MatcherAssert.assertThat(String.format("Cannot GET Person by ID=%s", randomIndex),
            personDao.get(randomIndex),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testGetPersonNotExists() {
        Serializable index = all.size() + 1L;
        MatcherAssert.assertThat(String.format("It was expected not to GET any Object by ID=%s", index),
            personDao.get(index),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testLoadPerson() {
        Serializable randomIndex = all.get(r.nextInt(all.size())).getId();
        MatcherAssert.assertThat(String.format("Cannot LOAD Person by ID=%s", randomIndex),
            personDao.load(randomIndex),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testLoadPersonNotExists() {
        Serializable index = all.size() + 10L;
        Assertions.assertThrows(
            ObjectNotFoundException.class,
            () -> personDao.load(index).toString(),
            "Expected org.hibernate.ObjectNotFoundException!");
    }

    @Test
    @SneakyThrows
    void testDelete() {
        Person toDelete = new Person();
        toDelete.setName(new RandomStrings(NAME).get());
        toDelete.setSurname(new RandomStrings(SURNAME).get());
        toDelete.setAge(new Random().nextInt(99 - 1) + 1);

        personDao.saveOrUpdate(toDelete); /* 1. Save object in DB */
        Assertions.assertNotNull(toDelete.getId(), String.format("%s was not saved in data base", toDelete));
        personDao.delete(toDelete); /* 2. Delete object from DB */

        MatcherAssert.assertThat(String.format("Expected %s to be deleted from db", toDelete),
            personDao.get(toDelete.getId()), /* 3. Try to get deleted object from DB */
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testGetAll() {
        List<Person> queried = new ArrayList<>(personDao.getAll(Person.class));
        all.forEach(p ->
            MatcherAssert.assertThat(String.format("%s does not exists in database.", p),
                queried.contains(p),
                Matchers.is(Boolean.TRUE)
            ));
    }

    @Test
    @SneakyThrows
    void testGetIds() {
        List<Long> collectedIds = all.stream().map(Person::getId).collect(Collectors.toList());
        List<Serializable> queried = new ArrayList<>(personDao.getIds());
        collectedIds.forEach(id -> {
            MatcherAssert.assertThat(String.format("%s does not exists in database.", id),
                queried.contains(id),
                Matchers.is(Boolean.TRUE)
            );
        });
    }

    @Test
    @SneakyThrows
    void testSessionFlush() {
        Person toTestFlush = new Person();
        toTestFlush.setName("TEST_FLUSH_NAME");
        toTestFlush.setSurname("TEST_FLUSH_SURNAME");
        toTestFlush.setAge(30);
        personDao.saveOrUpdate(toTestFlush);
        personDao.sessionFlushDemo(toTestFlush.getId(), new RandomStrings(NAME).get());
    }

    @Test
    @SneakyThrows
    void testSessionRefresh() {
        personDao.sessionRefreshDemo(all.iterator().next().getId(), new RandomStrings(NAME).get());
    }
}