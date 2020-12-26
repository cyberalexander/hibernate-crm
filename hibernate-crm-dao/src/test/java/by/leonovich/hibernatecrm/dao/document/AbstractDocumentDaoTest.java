package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.DocumentDao;
import by.leonovich.hibernatecrm.mappings.joinedtable.Document;
import by.leonovich.hibernatecrm.mappings.joinedtable.DrivingLicense;
import by.leonovich.hibernatecrm.mappings.joinedtable.Passport;
import by.leonovich.hibernatecrm.tools.MagicList;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created : 10/12/2020 10:13
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractDocumentDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractDocumentDaoTest.class);
    protected static final Dao<Document> dao = new DocumentDao();
    protected static final MagicList<Document> allDocuments = new MagicList<>();
    protected static final MagicList<DrivingLicense> drivingLicenses = new MagicList<>();
    protected static final MagicList<Passport> passports = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        drivingLicenses.addAll(Stream.generate(DrivingLicense::init).limit(10).collect(Collectors.toList()));
        passports.addAll(Stream.generate(Passport::init).limit(10).collect(Collectors.toList()));

        allDocuments.addAll(drivingLicenses);
        allDocuments.addAll(passports);
        Collections.shuffle(allDocuments);
        allDocuments.forEach(AbstractDocumentDaoTest::persistEach);
    }

    @SneakyThrows
    private static Document persistEach(Document doc) {
        dao.saveOrUpdate(doc);
        LOG.info("{}", doc);
        return doc;
    }

    /**
     * Method implemented in order to just "hide" {@link by.leonovich.hibernatecrm.dao.DaoException}
     */
    @SneakyThrows
    public Document daoGet(Serializable id) {
        return dao.get(id);
    }
}
