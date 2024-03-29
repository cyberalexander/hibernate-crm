package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestDaoConfiguration;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.common.random.RandomString;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.leonovich.hibernatecrm.TestConstants.MAIN_LIMIT;

/**
 * Created : 06/12/2020 20:47
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDaoConfiguration.class})
class HibernateAbilitiesTest {
    private static final Logger log = LogManager.getLogger(HibernateAbilitiesTest.class);
    private static final MagicList<Person> testData = new MagicList<>();

    @Autowired
    private SessionFactory hibernate;

    @BeforeEach
    @SneakyThrows
    public void postConstruct() {
        if (CollectionUtils.isEmpty(testData)) {
            testData.addAll(
                Stream.generate(Person::init).limit(MAIN_LIMIT).collect(Collectors.toList())
            );
            Session session = hibernate.openSession();
            Transaction t = session.beginTransaction();
            for (Person p : testData) {
                session.save(p);
            }
            t.commit();
        }
    }

    @Test
    @SneakyThrows
    void testContains() {
        Session session = hibernate.openSession();
        Person notInContext = testData.randomEntity();

        MatcherAssert.assertThat("Object should not be associated with session",
            session.contains(notInContext),
            Matchers.is(Boolean.FALSE)
        );
    }

    @Test
    @SneakyThrows
    void testEvict() {
        Session session = hibernate.openSession();
        Long id = testData.randomEntity().getId();
        Transaction t = session.beginTransaction();
        Person test = session.get(Person.class, id); //attached to context
        session.evict(test); //removed from context

        MatcherAssert.assertThat("Object expected to be removed from session",
            session.contains(test),
            Matchers.is(Boolean.FALSE)
        );
        t.commit();
    }

    /**
     * Explicitly flushing information from context to database. If entity was changed in context,
     * changes will be stored in database.
     */
    @Test
    @SneakyThrows
    void testFlush() {
        Person detached = testData.randomEntity();
        Session session = hibernate.openSession();
        log.info("isDirty={}, in memory : {}", session.isDirty(), detached);

        Transaction t = session.beginTransaction();
        Person flushed = session.get(Person.class, detached.getId());
        log.info("isDirty={}, from session : {}", session.isDirty(), flushed);
        flushed.setName("TEST_FLUSH_NAME");
        flushed.setSurname("TEST_FLUSH_SURNAME");
        log.info("isDirty={}, modified in context : {}", session.isDirty(), flushed);
        session.flush();
        t.commit();

        t = session.beginTransaction();
        Person queried = session.load(Person.class, detached.getId());
        t.commit();
        MatcherAssert.assertThat("Flushed changes expected to be persisted in database",
            flushed.equals(queried),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat("Detached object expected to be different from flushed after change",
            queried.equals(detached),
            Matchers.is(Boolean.FALSE)
        );
    }

    /**
     * Refresh information about entity(ies) in context with actual data from database. If entity was changed
     * in context, refresh will erase those changes.
     */
    @Test
    @SneakyThrows
    void testRefresh() {
        Person detached = testData.randomEntity();
        Session session = hibernate.openSession();
        log.info("isDirty={}, in memory : {}", session.isDirty(), detached);

        Transaction t = session.beginTransaction();
        Person refreshed = session.get(Person.class, detached.getId());
        log.info("isDirty={}, from session : {}", session.isDirty(), refreshed);
        refreshed.setName("TEST_REFRESH_NAME");
        refreshed.setSurname("TEST_REFRESH_SURNAME");
        log.info("isDirty={}, modified in context : {}", session.isDirty(), refreshed);
        session.refresh(refreshed);
        t.commit();
        log.info("isDirty={}, after refresh : {}", session.isDirty(), refreshed);

        t = session.beginTransaction();
        Person queried = session.load(Person.class, detached.getId());
        t.commit();

        MatcherAssert.assertThat("Refreshed should be equal to it's state in database",
            refreshed.equals(queried),
            Matchers.is(Boolean.TRUE)
        );
    }

    @Test
    void testPersist() {
        Person random = testData.randomEntity();
        Session session = hibernate.openSession();
        Transaction t = session.beginTransaction();
        log.info("isDirty={}, before modify : {}", session.isDirty(), random);

        random = session.get(Person.class, random.getId());
        random.setName("MODIFIED_" + RandomString.NAME.get());
        log.info("isDirty={}, after modify : {}", session.isDirty(), random);
        session.persist(random);
        t.commit();

        t = session.beginTransaction();
        Person persisted = session.get(Person.class, random.getId());
        t.commit();

        log.info("isDirty={}, persisted : {}", session.isDirty(), persisted);
        Assertions.assertEquals(random.getName(), persisted.getName());
    }

    /**
     * {@link Session#persist} will throw {@link PersistenceException} in case in method passed Entity, which is not
     * associated with session and and it's id parameter is not null. Hibernate will think, this entity already stored
     * in database.
     * Lik in this method we are calling persist for existing in database object, but this object is not attached to the
     * session. Or like in {@link HibernateAbilitiesTest#testPersistThrowException2()} where we creating new object
     * and populating it's id with some value (like it was already stored in database)
     */
    @Test
    void testPersistThrowException() {
        Person random = testData.randomEntity();
        Session session = hibernate.openSession();

        Transaction t = session.beginTransaction();
        log.info("isDirty={}, before modify : {}", session.isDirty(), random);
        random.setName("MODIFIED_" + RandomString.NAME.get());
        log.info("isDirty={}, after modify : {}", session.isDirty(), random);

        Assertions.assertThrows(
            PersistenceException.class,
            () -> session.persist(random),
            "Expected [javax.persistence.PersistenceException: org.hibernate.PersistentObjectException: " +
                "detached entity passed to persist]!");

        t.commit();
    }

    @Test
    void testPersistThrowException2() {
        Person newPerson = new Person();
        newPerson.setId(new Random().nextLong());

        Session session = hibernate.openSession();
        Transaction t = session.beginTransaction();
        log.info("isDirty={}, before persist : {}", session.isDirty(), newPerson);

        Assertions.assertThrows(
            PersistenceException.class,
            () -> session.persist(newPerson),
            "Expected [javax.persistence.PersistenceException: org.hibernate.PersistentObjectException: " +
                "detached entity passed to persist]!");

        t.commit();
    }
}
