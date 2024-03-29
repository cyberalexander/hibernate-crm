package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.TestDaoConfiguration;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.mappings.annotation.Author;
import by.leonovich.hibernatecrm.mappings.annotation.Typewriter;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.Objects;

/**
 * Created : 13/01/2021 16:55
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDaoConfiguration.class})
@Transactional
@Commit
class TypewriterDaoTest implements BaseDaoTest<Typewriter> {
    private static final MagicList<Typewriter> typewriters = new MagicList<>();

    @Autowired
    private Dao<Typewriter> dao;

    @Test
    @SneakyThrows
    void testSaveCascade() {
        Typewriter typewriter = Typewriter.initWithOneToOne();
        dao().save(typewriter);
        log.debug("Relation saved as well? {}", Objects.nonNull(typewriter.getAuthor().getId()));
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

    //@Test TODO fix this test
    @SneakyThrows
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
        return dao;
    }

    @Override
    public MagicList<Typewriter> entities() {
        return typewriters;
    }

    @Override
    public Typewriter generate() {
        return Typewriter.init();
    }
}
