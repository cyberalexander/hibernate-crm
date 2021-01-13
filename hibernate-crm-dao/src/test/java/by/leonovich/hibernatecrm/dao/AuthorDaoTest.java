package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.annotation.Author;
import by.leonovich.hibernatecrm.annotation.Portfolio;
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
class AuthorDaoTest implements BaseDaoTest<Author> {
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

    @Test
    @SneakyThrows
    void testSaveCascade() {
        Author a = Author.initCascade();
        dao().save(a);
        LOG.debug("Relation saved as well? {}", Objects.nonNull(a.getPortfolio().getId()));
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            dao().get(a.getId()).getPortfolio().getId(),
            Matchers.equalTo(a.getPortfolio().getId())
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade() {
        Author a = Author.initCascade();
        Portfolio portfolio = a.getPortfolio();
        dao().saveOrUpdate(a);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, portfolio, a),
            portfolio.getId(),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Author a = Author.initCascade();
        dao().save(a);
        Portfolio portfolio = ((Author) a.modifyCascade()).getPortfolio();
        dao().saveOrUpdate(a);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, portfolio, a),
            dao().get(a.getId()).getPortfolio(),
            Matchers.equalTo(portfolio)
        );
    }

    @Test
    @SneakyThrows
    void testDeleteCascade() {
        Author a = Author.initCascade();
        dao().save(a);
        Portfolio portfolio = a.getPortfolio();
        dao().delete(a);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_DELETE_CASCADE, portfolio, a),
            new PortfolioDao().get(portfolio.getId()),
            Matchers.nullValue()
        );
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
