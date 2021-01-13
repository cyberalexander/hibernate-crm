package by.leonovich.hibernatecrm.dao.person;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.PhoneNumberDao;
import by.leonovich.hibernatecrm.mappings.singletable.Address;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.mappings.singletable.PhoneNumber;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Created : 27/11/2020 09:54
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class PersonDaoTest extends CommonPersonDaoTest implements BaseDaoTest<Person> {
    private static final Logger LOG = LoggerFactory.getLogger(PersonDaoTest.class);

    @Test
    @SneakyThrows
    void testSaveCascade() {
        Person p = Person.init();
        dao().save(p);
        LOG.debug("Relation saved as well? {}", Objects.nonNull(p.getPhoneNumber().getPersonId()));
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            dao().get(p.getId()).getPhoneNumber().getPersonId(),
            Matchers.equalTo(p.getPhoneNumber().getPersonId())
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade() {
        Person p = Person.init();
        PhoneNumber phoneNumber = p.getPhoneNumber();
        dao().saveOrUpdate(p);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, phoneNumber, p),
            phoneNumber.getPersonId(),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Person p = Person.init();
        dao().save(p);
        PhoneNumber modifiedCascade = p.modifyCascade().getPhoneNumber();
        dao().saveOrUpdate(p);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, modifiedCascade, p),
            dao().get(p.getId()).getPhoneNumber(),
            Matchers.equalTo(modifiedCascade)
        );
    }

    @Test
    @SneakyThrows
    void testDeleteCascade() {
        Person p = Person.init();
        dao().save(p);
        PhoneNumber phoneNumber = p.getPhoneNumber();
        dao().delete(p);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_DELETE_CASCADE, phoneNumber, p),
            new PhoneNumberDao().get(p.getId()),
            Matchers.nullValue()
        );
    }


    /**
     * Test {@link Address#getPostIndex()} "hibernate-calculable" field
     */
    @Test
    @SneakyThrows
    void testPersonAddressColumnIndex() {
        Address address = dao().get(allPersons.randomEntity().getId()).getHomeAddress();
        String expected = address.getCountryCode() + "_" + address.getCountry();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_TEST_INDEX, expected),
            address.getPostIndex(),
            Matchers.equalTo(expected)
        );
    }

    @Override
    public Dao<Person> dao() {
        return personDao;
    }

    @Override
    public MagicList<Person> entities() {
        return allPersons;
    }
}
