package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.annotation.Book;
import by.leonovich.hibernatecrm.annotation.Library;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;
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
            Stream.generate(Library::initWitOneToMany)
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

    @Test
    @SneakyThrows
    void testSaveCascade() {
        Library library = Library.initWitOneToMany();
        dao().save(library);
        LOG.debug("Relation saved as well? {}", Objects.nonNull(library.getBooks().iterator().next().getId()));
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            dao().get(library.getId()).getBooks().iterator().next().getId(),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade() {
        Library library = Library.initWitOneToMany();
        dao().saveOrUpdate(library);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, library.getBooks(), library),
            dao().get(library.getId()).getBooks().size(),
            Matchers.is(2)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Library library = entities().randomEntity();
        Set<Book> books = ((Library) library.modifyCascade()).getBooks();
        dao().saveOrUpdate(library);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, books, library),
            dao().get(library.getId()).getBooks(),
            Matchers.equalTo(books)
        );
    }

    @Test
    @SneakyThrows
    void testDeleteCascade() {
        Library library = Library.initWitOneToMany();
        dao().save(library);
        Assertions.assertNotNull(library.getId(), TestConstants.M_SAVE);
        Book book = library.getBooks().iterator().next();

        dao().delete(library);

        MatcherAssert.assertThat(
            String.format(TestConstants.M_DELETE_CASCADE, book, library),
            new BookDao().get(book.getId()),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testDeleteOrphan() {
        Library library = Library.initWitOneToMany();
        dao().save(library);
        Assertions.assertNotNull(library.getId(), TestConstants.M_SAVE);
        library = dao().get(library.getId()); //Important to load entity in context to successfully delete orphan

        Book orphan = library.getBooks().iterator().next();
        boolean removed = library.writeOffBook(orphan);
        LOG.debug("Book {} removed from Library {}? {}",orphan.getId(), library.getId(),  removed);
        dao().saveOrUpdate(library);

        MatcherAssert.assertThat(String.format(TestConstants.M_DELETE_ORPHAN, orphan, library),
            new BookDao().get(orphan.getId()),
            Matchers.nullValue()
        );
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
