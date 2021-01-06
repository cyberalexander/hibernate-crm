package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.DocumentDao;
import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import by.leonovich.hibernatecrm.mappings.joinedtable.Document;
import by.leonovich.hibernatecrm.mappings.joinedtable.DrivingLicense;
import by.leonovich.hibernatecrm.mappings.joinedtable.Passport;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    private static final MagicList<Document> allDocuments = new MagicList<>();
    protected static final MagicList<DrivingLicense> drivingLicenses = new MagicList<>();
    protected static final MagicList<Passport> passports = new MagicList<>();

    @BeforeAll
    @SneakyThrows
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

        Dao<Document> dao = new DocumentDao();
        for (Document doc : allDocuments) {
            dao.save(doc);
        }
        HibernateUtil.getInstance().closeSession();
    }

    @AfterEach
    void tearDown() {
        //Approach: Session opened in DAO method; session closed here after each @test method execution
        HibernateUtil.getInstance().closeSession();
    }

    @Test
    void testDataReady() {
        MatcherAssert.assertThat(
            "Test data is not ready!",
            CollectionUtils.isEmpty(allDocuments),
            Matchers.is(Boolean.FALSE)
        );
    }
}
