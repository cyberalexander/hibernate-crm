package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.annotation.Author;
import by.leonovich.hibernatecrm.annotation.Book;
import by.leonovich.hibernatecrm.annotation.Portfolio;
import by.leonovich.hibernatecrm.annotation.Typewriter;
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

import java.util.List;
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
class AuthorDaoTest implements BaseDaoTest<Author> {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorDaoTest.class);
    private static final Dao<Author> authorDao = new AuthorDao();
    private static final MagicList<Author> authors = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        authors.addAll(
            Stream.generate(Author::initCascade)
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
    void testSaveCascade_PortFolio() {
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
    void testSaveCascade_TypeWriter() {
        Author a = Author.initCascade();
        dao().save(a);
        LOG.debug("Relation saved as well? {}", Objects.nonNull(a.getTypewriter().getId()));
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            dao().get(a.getId()).getTypewriter().getId(),
            Matchers.equalTo(a.getTypewriter().getId())
        );
    }

    @Test
    @SneakyThrows
    void testSaveCascade_Books() {
        Author a = Author.initCascade();
        dao().save(a);
        LOG.debug("Relation saved as well? {}", Objects.nonNull(a.getBooks().iterator().next().getId()));
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            dao().get(a.getId()).getBooks().iterator().next().getId(),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade_Portfolio() {
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
    void testSaveOrUpdate_SaveCascade_Typewriter() {
        Author a = Author.initCascade();
        Typewriter typewriter = a.getTypewriter();
        dao().saveOrUpdate(a);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, typewriter, a),
            typewriter.getId(),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade_Books() {
        Author a = Author.initCascade();
        dao().saveOrUpdate(a);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, a.getBooks(), a),
            dao().get(a.getId()).getBooks().size(),
            Matchers.is(2)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade_Portfolio() {
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
    void testSaveOrUpdate_UpdateCascade_Typewriter() {
        Author a = Author.initCascade();
        dao().save(a);
        Typewriter typewriter = ((Author) a.modifyCascade()).getTypewriter();
        dao().saveOrUpdate(a);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, typewriter, a),
            dao().get(a.getId()).getTypewriter(),
            Matchers.equalTo(typewriter)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade_Books() {
        Author a = entities().randomEntity();
        Set<Book> updated = ((Author) a.modifyCascade()).getBooks();
        dao().saveOrUpdate(a);
        Set<Book> queried = dao().get(a.getId()).getBooks();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, updated, a),
            queried,
            Matchers.equalTo(updated)
        );
    }

    @Test
    @SneakyThrows
    void testDeleteCascade_Portfolio() {
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

    @Test
    @SneakyThrows
    void testDeleteCascade_Typewriter() {
        Author a = Author.initCascade();
        dao().save(a);
        Typewriter typewriter = a.getTypewriter();
        dao().delete(a);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_DELETE_CASCADE, typewriter, a),
            new TypewriterDao().get(typewriter.getId()),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testDeleteCascade_Books() {
        Author a = Author.initCascade();
        dao().save(a);
        Book relatedBook = a.getBooks().iterator().next();
        dao().delete(a);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_DELETE_CASCADE, relatedBook, a),
            new BookDao().get(relatedBook.getId()),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testGetAuthorByNameCriteria() {
        Author author = entities().randomEntity();
        List<Author> sameNameAuthors = ((AuthorDao) dao()).getAuthorByNameCriteria(author.getName());
        sameNameAuthors.forEach(a -> MatcherAssert.assertThat(
            String.format(TestConstants.M_TEST_SELECT_BY_NAME, author.getName(), a.getName()),
            a.getName(),
            Matchers.equalTo(author.getName())
        ));
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testGetAuthorByNameHql() {
        Author author = entities().randomEntity();
        List<Author> sameNameAuthors = ((AuthorDao) dao()).getAuthorByNameHql(author.getName());
        sameNameAuthors.forEach(a -> MatcherAssert.assertThat(
            String.format(TestConstants.M_TEST_SELECT_BY_NAME, author.getName(), a.getName()),
            a.getName(),
            Matchers.equalTo(author.getName())
        ));
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
