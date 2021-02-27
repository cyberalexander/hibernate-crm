package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.annotation.Author;
import by.leonovich.hibernatecrm.annotation.Portfolio;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

/**
 * Created : 08/01/2021 21:05
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations= "classpath:DaoContext.xml")
class PortfolioDaoTest implements BaseDaoTest<Portfolio> {
    private static final Logger LOG = LoggerFactory.getLogger(PortfolioDaoTest.class);
    private static final MagicList<Portfolio> portfolios = new MagicList<>();

    @Autowired
    private Dao<Portfolio> dao;
    @Autowired
    private AuthorDao authorDao;

    @Test
    @SneakyThrows
    void testSaveCascade() {
        Portfolio p = Portfolio.initWithOneToOne();
        dao().save(p);
        LOG.debug("Relation saved as well? {}", Objects.nonNull(p.getAuthor().getId()));
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            dao().get(p.getId()).getAuthor().getId(),
            Matchers.equalTo(p.getAuthor().getId())
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade() {
        Portfolio p = Portfolio.initWithOneToOne();
        Author author = p.getAuthor();
        dao().saveOrUpdate(p);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, author, p),
            author.getId(),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Portfolio p = Portfolio.initWithOneToOne();
        dao().save(p);
        Author author = ((Portfolio) p.modifyCascade()).getAuthor();
        dao().saveOrUpdate(p);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, author, p),
            dao().get(p.getId()).getAuthor(),
            Matchers.equalTo(author)
        );
    }

    @Test
    @SneakyThrows
    void testDeleteCascade() {
        Portfolio p = Portfolio.initWithOneToOne();
        dao().save(p);
        Author author = p.getAuthor();

        /* to avoid hibernate exception, need to brake relation before Portfolio deletion, as Portfolio has cascadeType=SAVE_UPDATE */
        author.setPortfolio(null);
        authorDao.saveOrUpdate(author);

        dao().delete(p);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_DELETE_CASCADE_AND_KEEP_RELATION, author, p),
            authorDao.get(author.getId()),
            Matchers.notNullValue()
        );
    }

    @Override
    public Dao<Portfolio> dao() {
        return dao;
    }

    @Override
    public MagicList<Portfolio> entities() {
        return portfolios;
    }

    @Override
    public Portfolio generate() {
        return Portfolio.init();
    }
}
