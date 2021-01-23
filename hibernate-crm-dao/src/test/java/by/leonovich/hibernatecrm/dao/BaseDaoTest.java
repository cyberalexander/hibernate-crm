package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.common.model.Automated;
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
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created : 02/01/2021 15:37
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public interface BaseDaoTest<T extends Automated> {
    Logger LOG = LoggerFactory.getLogger(BaseDaoTest.class);

    @Test
    @SneakyThrows
    default void testPersist() {
        T newEntity = newInstance().populate();
        dao().persist(newEntity);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_PERSIST, newEntity),
            dao().get(newEntity.getId()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    default void testSave() {
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            dao().save(newInstance().populate()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    default void testSaveOrUpdate_Save() {
        T newEntity = newInstance().populate();
        dao().saveOrUpdate(newEntity);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE, newEntity),
            dao().get(newEntity.getId()),
            Matchers.equalTo(newEntity)
        );
    }

    @Test
    @SneakyThrows
    default void testSaveOrUpdate_Update() {
        T modified = entities().randomEntity().modify();
        dao().saveOrUpdate(modified);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, modified),
            dao().get(modified.getId()),
            Matchers.equalTo(modified)
        );
    }

    @Test
    @SneakyThrows
    default void testGet() {
        T entity = entities().randomEntity();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, entity.getClass().getSimpleName(), entity.getId()),
            dao().get(entity.getId()),
            Matchers.isA(getEntityClass())
        );
    }

    @Test
    @SneakyThrows
    default void testGet_NotExists() {
        Serializable index = entities().lastElement().incrementIdAndGet();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET_NOT_EXISTS, index),
            dao().get(index),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    default void testLoad() {
        T entity = entities().randomEntity();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_LOAD, entity.getClass().getSimpleName(), entity.getId()),
            dao().load(entity.getId()),
            Matchers.notNullValue()
        );
    }

    /**
     * Method {@link Session#load} is different from {@link Session#get}
     * In case of "get", when entity not exists in database, hibernate returns null. When in case of "load"
     * hibernate will not query entity immediately but will just return proxy. And when we try to access any property
     * of that proxy, hibernate will immediately execute select to database and throw {@link ObjectNotFoundException}
     * if entity won't be found in database.
     */
    @Test
    @SneakyThrows
    default void testLoad_NotExists() {
        Serializable index = entities().lastElement().incrementIdAndGet();
        T loaded = dao().load(index);
        Assertions.assertThrows(
            ObjectNotFoundException.class,
            loaded::toString,
            TestConstants.M_LOAD_EXCEPTION);
    }

    /**
     * 1. Save Entity in database
     * 2. Assert that Entity persisted (has assigned Id)
     * 4. Delete Entity from database
     * 5. Assert that Entity does not exists in database
     */
    @Test
    @SneakyThrows
    default void testDelete() {
        T entity = newInstance().populate();
        dao().saveOrUpdate(entity); //[1]
        Assertions.assertNotNull(entity.getId(), TestConstants.M_SAVE); //[2]
        dao().delete(entity); //[3]
        //[4]
        MatcherAssert.assertThat(String.format(TestConstants.M_DELETE, entity),
            dao().get(entity.getId()),
            Matchers.nullValue()
        );
    }

    /**
     * Comparing IDs here, because other information might be changed at the time other tests are running,
     * like {@link BaseDaoTest#testSaveOrUpdate_Update()}
     */
    @Test
    @SneakyThrows
    default void testGetAll() {
        List<Serializable> queried = dao().getAll(getEntityClass()).stream()
            .map(Automated::getId)
            .collect(Collectors.toList());
        entities().stream().map(Automated::getId).forEach(entityId ->
            MatcherAssert.assertThat(
                String.format(TestConstants.M_GET_ALL, getEntityClass().getSimpleName(), entityId),
                queried.contains(entityId),
                Matchers.is(Boolean.TRUE)
            ));
    }

    @Test
    @SneakyThrows
    default void testGetIds() {
        List<Serializable> queried = dao().getIds();
        entities().stream().map(Automated::getId).forEach(id -> MatcherAssert.assertThat(
            String.format(TestConstants.M_GET_IDS, id),
            queried.contains(id),
            Matchers.is(Boolean.TRUE))
        );
    }

    @Test
    @SneakyThrows
    default void testGetLastIndex() {
        Serializable maxId = dao().getIds().lastElement();
        Serializable queriedMaxId = dao().getLastIndex();
        LOG.info("expected={}; actual={}", maxId, queriedMaxId);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_TEST_LAST_INDEX, getEntityClass().getSimpleName(), queriedMaxId, maxId),
            maxId,
            Matchers.equalTo(queriedMaxId)
        );
    }

    Dao<T> dao();

    MagicList<T> entities();

    @SuppressWarnings("unchecked")
    default Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }

    @SneakyThrows
    default T newInstance() {
        return getEntityClass().getDeclaredConstructor().newInstance();
    }
}