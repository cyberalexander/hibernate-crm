package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.annotation.Author;
import by.leonovich.hibernatecrm.annotation.Typewriter;
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

import javax.persistence.PersistenceException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.leonovich.hibernatecrm.TestConstants.LIMIT;

/**
 * Created : 13/01/2021 16:55
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class TypewriterDaoTest implements BaseDaoTest<Typewriter> {
    private static final Logger LOG = LoggerFactory.getLogger(TypewriterDaoTest.class);
    private static final Dao<Typewriter> typewriterDao = new TypewriterDao();
    private static final MagicList<Typewriter> typewriters = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        typewriters.addAll(
            Stream.generate(Typewriter::init)
                .limit(LIMIT)
                .map(TypewriterDaoTest::persist)
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
        Typewriter typewriter = Typewriter.initWithOneToOne();
        dao().save(typewriter);
        LOG.debug("Relation saved as well? {}", Objects.nonNull(typewriter.getAuthor().getId()));
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            dao().get(typewriter.getId()).getAuthor().getId(),
            Matchers.equalTo(typewriter.getAuthor().getId())
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade() {
        Typewriter typewriter = Typewriter.initWithOneToOne();
        Author author = typewriter.getAuthor();
        dao().saveOrUpdate(typewriter);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, author, typewriter),
            author.getId(),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Typewriter typewriter = Typewriter.initWithOneToOne();
        dao().save(typewriter);
        Author modified = ((Typewriter) typewriter.modifyCascade()).getAuthor();
        dao().saveOrUpdate(typewriter);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, modified, typewriter),
            dao().get(typewriter.getId()).getAuthor(),
            Matchers.equalTo(modified)
        );
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testDeleteCascade() {
        Typewriter typewriter = Typewriter.initWithOneToOne();
        dao().save(typewriter);
        Assertions.assertThrows(
            PersistenceException.class,
            () -> dao().delete(typewriter),
            "Typewriter is not the owner of relation and in has cascade=save_update, " +
                "so exception expected to be thrown where deleting typewriter with existing reference to author.");
    }

    @Override
    public Dao<Typewriter> dao() {
        return typewriterDao;
    }

    @Override
    public MagicList<Typewriter> entities() {
        return typewriters;
    }

    @SneakyThrows
    private static Typewriter persist(Typewriter typewriter) {
        typewriterDao.save(typewriter);
        LOG.info("{}", typewriter);
        return typewriter;
    }
}
