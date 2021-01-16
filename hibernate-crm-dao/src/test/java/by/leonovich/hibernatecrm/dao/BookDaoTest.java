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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
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
class BookDaoTest implements BaseDaoTest<Book> {
    private static final Logger LOG = LoggerFactory.getLogger(BookDaoTest.class);
    private static final Dao<Book> bookDao = new BookDao();
    private static final MagicList<Book> books = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        books.addAll(
            Stream.generate(Book::init)
                .limit(LIMIT)
                .map(BookDaoTest::persist)
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
        Book book = Book.initWithManyToOne();
        dao().save(book);
        LOG.debug("Relation saved as well? {}", Objects.nonNull(book.getLibrary().getId()));
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            dao().get(book.getId()).getLibrary().getId(),
            Matchers.equalTo(book.getLibrary().getId())
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade() {
        Book book = Book.initWithManyToOne();
        Library library = book.getLibrary();
        dao().saveOrUpdate(book);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, library, book),
            library.getId(),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Book book = Book.initWithManyToOne();
        dao().save(book);
        Library library = ((Book) book.modifyCascade()).getLibrary();
        dao().saveOrUpdate(book);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, library, book),
            dao().get(book.getId()).getLibrary(),
            Matchers.equalTo(library)
        );
    }

    @Test
    @SneakyThrows
    void testDeleteCascade() {
        Book book = Book.initWithManyToOne();
        dao().save(book);
        Library library = book.getLibrary();

        /* to avoid hibernate exception, need to brake relation before Portfolio deletion, as Portfolio has cascadeType=SAVE_UPDATE */
        library.setBooks(null);
        new LibraryDao().saveOrUpdate(library);

        dao().delete(book);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_DELETE_CASCADE_AND_KEEP_RELATION, library, book),
            new LibraryDao().get(library.getId()),
            Matchers.notNullValue()
        );
    }

    @Override
    public Dao<Book> dao() {
        return bookDao;
    }

    @Override
    public MagicList<Book> entities() {
        return books;
    }

    @SneakyThrows
    private static Book persist(Book book) {
        bookDao.save(book);
        LOG.info("{}", book);
        return book;
    }
}
