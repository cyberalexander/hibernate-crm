package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.annotation.Author;
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
public class AuthorDaoTest implements BaseDaoTest<Author> {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorDaoTest.class);
    private static final Dao<Author> authorDao = new AuthorDao();
    private static final MagicList<Author> authors = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        authors.addAll(
            Stream.generate(Author::init)
                .limit(LIMIT)
                .map(AuthorDaoTest::persist)
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
    public Dao<Author> dao() {
        return authorDao;
    }

    @Override
    public MagicList<Author> entities() {
        return authors;
    }

    @SneakyThrows
    private static Author persist(Author author) {
        authorDao.save(author);
        LOG.info("{}", author);
        return author;
    }
}
