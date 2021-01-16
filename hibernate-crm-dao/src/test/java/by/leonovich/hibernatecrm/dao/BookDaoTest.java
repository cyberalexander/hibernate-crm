package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.annotation.Book;
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
