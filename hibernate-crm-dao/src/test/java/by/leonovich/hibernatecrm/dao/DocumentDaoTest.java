package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.joinedtable.Document;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

/**
 * Created : 10/12/2020 10:12
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class DocumentDaoTest extends AbstractDocumentDaoTest {

    @Test
    @SneakyThrows
    void testPersist() {
        Document d = Document.init();
        dao.persist(d);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_PERSIST, d),
            dao.get(d.getId()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSave() {
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            dao.save(Document.init()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdateSave() {
        Document toSave = Document.init();
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
        Document toUpdate = allDocuments.get(randIndex()).populate();
        toUpdate.setDocumentNumber("UPDATE_" + RandomString.DOCUMENT_NUMBER.get() + "_" + toUpdate.getId());
        dao.saveOrUpdate(toUpdate);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, toUpdate),
            dao.get(toUpdate.getId()),
            Matchers.equalTo(toUpdate)
        );
    }

    @Test
    @SneakyThrows
    void testGetDocument() {
        Serializable randomIndex = allDocuments.get(randIndex()).getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, Document.class.getSimpleName(), randomIndex),
            dao.get(randomIndex),
            Matchers.notNullValue()
        );
    }
}

