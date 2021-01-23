package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.DocumentDao;
import by.leonovich.hibernatecrm.mappings.joinedtable.Document;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created : 23/01/2021 14:45
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class DocumentDaoTest extends CommonDocumentDaoTest implements BaseDaoTest<Document> {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentDaoTest.class);
    private static final Dao<Document> documentDao = new DocumentDao();

    @Test
    @SneakyThrows
    void testGetExpiringThisYearDocumentsCriteria() {
        List<Document> expiresThisYear = dao().getAll(Document.class).stream()
            .filter(doc -> doc.getExpirationDate().getYear() == LocalDate.now().getYear())
            .collect(Collectors.toList());
        LOG.info("Expected : {} : {}", expiresThisYear.size(), expiresThisYear);
        List<Document> queried = ((DocumentDao) dao()).getExpiringThisYearDocumentsCriteria();
        LOG.info("Actual : {} : {}", queried.size(), queried);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_TEST_GET_EXPIRING_THIS_YEAR_DOCUMENTS, expiresThisYear.size(), queried.size()),
            queried,
            Matchers.equalTo(expiresThisYear)
        );
    }

    @Test
    @SneakyThrows
    void testGetExpiringThisYearDocumentsHql() {
        List<Document> expiresThisYear = dao().getAll(Document.class).stream()
            .filter(doc -> doc.getExpirationDate().getYear() == LocalDate.now().getYear())
            .collect(Collectors.toList());
        LOG.info("Expected : {} : {}", expiresThisYear.size(), expiresThisYear);
        List<Document> queried = ((DocumentDao) dao()).getExpiringThisYearDocumentsHql();
        LOG.info("Actual : {} : {}", queried.size(), queried);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_TEST_GET_EXPIRING_THIS_YEAR_DOCUMENTS, expiresThisYear.size(), queried.size()),
            queried,
            Matchers.equalTo(expiresThisYear)
        );
    }

    @Override
    public Dao<Document> dao() {
        return documentDao;
    }

    @Override
    public MagicList<Document> entities() {
        return allDocuments;
    }
}
