package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.mappings.joinedtable.Document;
import by.leonovich.hibernatecrm.mappings.joinedtable.DrivingLicense;
import by.leonovich.hibernatecrm.mappings.joinedtable.Passport;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;
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
    protected static final Random r = new Random();
    protected static final Dao<Document> dao = new DocumentDao();
    protected static List<Document> allDocuments;

    @BeforeAll
    static void beforeAll() {
        Stream<Document> documentStream = Stream.generate(Document::init).limit(25);
        Stream<DrivingLicense> licenseStream = Stream.generate(DrivingLicense::init).limit(30);
        Stream<Passport> passportStream = Stream.generate(Passport::init).limit(35);

        allDocuments = Stream.concat(Stream.concat(documentStream, licenseStream), passportStream).collect(Collectors.toList());
        Collections.shuffle(allDocuments);
        allDocuments.forEach(AbstractDocumentDaoTest::persistEach);
    }

    @SneakyThrows
    private static Document persistEach(Document doc) {
        dao.saveOrUpdate(doc);
        LOG.info("{}", doc);
        return doc;
    }

    protected int randIndex() {
        return r.nextInt(allDocuments.size() - 1);
    }
}
