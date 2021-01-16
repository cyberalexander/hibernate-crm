package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.annotation.Library;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.leonovich.hibernatecrm.TestConstants.LIMIT;

/**
 * Created : 08/01/2021 21:05
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class LibraryDaoTest implements BaseDaoTest<Library> {
    private static final Logger LOG = LoggerFactory.getLogger(LibraryDaoTest.class);
    private static final Dao<Library> libraryDao = new LibraryDao();
    private static final MagicList<Library> libraries = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        libraries.addAll(
            Stream.generate(Library::init)
                .limit(LIMIT)
                .map(LibraryDaoTest::persist)
                .collect(Collectors.toList())
        );
        HibernateUtil.getInstance().closeSession();
    }

    @AfterEach
    void tearDown() {
        //Approach: Session opened in DAO method; session closed here after each @test method execution
        HibernateUtil.getInstance().closeSession();
    }

    @Override
    public Dao<Library> dao() {
        return libraryDao;
    }

    @Override
    public MagicList<Library> entities() {
        return libraries;
    }

    @SneakyThrows
    private static Library persist(Library library) {
        libraryDao.save(library);
        LOG.info("{}", library);
        return library;
    }
}
