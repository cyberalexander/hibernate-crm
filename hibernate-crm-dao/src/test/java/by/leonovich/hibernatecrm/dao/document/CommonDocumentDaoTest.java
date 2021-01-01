package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.DocumentDao;
import by.leonovich.hibernatecrm.mappings.joinedtable.Document;
import by.leonovich.hibernatecrm.mappings.joinedtable.DrivingLicense;
import by.leonovich.hibernatecrm.mappings.joinedtable.Passport;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.leonovich.hibernatecrm.TestConstants.MAIN_LIMIT;

/**
 * Created : 10/12/2020 10:13
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class CommonDocumentDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(CommonDocumentDaoTest.class);
    protected static final Dao<Document> dao = new DocumentDao();
    protected static final MagicList<Document> allDocuments = new MagicList<>();
    protected static final MagicList<DrivingLicense> drivingLicenses = new MagicList<>();
    protected static final MagicList<Passport> passports = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        /* hint to do not invoke this method more than once */
        if (CollectionUtils.isNotEmpty(allDocuments)) {
            return;
        }
        drivingLicenses.addAll(Stream.generate(DrivingLicense::init).limit(MAIN_LIMIT).collect(Collectors.toList()));
        passports.addAll(Stream.generate(Passport::init).limit(MAIN_LIMIT).collect(Collectors.toList()));

        allDocuments.addAll(drivingLicenses);
        allDocuments.addAll(passports);
        Collections.shuffle(allDocuments);
        allDocuments.forEach(CommonDocumentDaoTest::persist);
    }

    @SneakyThrows
    private static Document persist(Document doc) {
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
