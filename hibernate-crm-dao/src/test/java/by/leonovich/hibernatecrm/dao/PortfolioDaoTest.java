package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.annotation.Portfolio;
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
public class PortfolioDaoTest implements BaseDaoTest<Portfolio> {
    private static final Logger LOG = LoggerFactory.getLogger(PortfolioDaoTest.class);
    private static final Dao<Portfolio> portfolioDao = new PortfolioDao();
    private static final MagicList<Portfolio> portfolios = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        portfolios.addAll(
            Stream.generate(Portfolio::init)
                .limit(LIMIT)
                .map(PortfolioDaoTest::persist)
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
    public Dao<Portfolio> dao() {
        return portfolioDao;
    }

    @Override
    public MagicList<Portfolio> entities() {
        return portfolios;
    }

    @SneakyThrows
    private static Portfolio persist(Portfolio portfolio) {
        portfolioDao.save(portfolio);
        LOG.info("{}", portfolio);
        return portfolio;
    }
}
