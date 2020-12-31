package by.leonovich.hibernatecrm.mappings;

import by.leonovich.hibernatecrm.tools.Constants;
import by.leonovich.hibernatecrm.tools.RandomString;

import java.io.Serializable;

/**
 * Created : 27/12/2020 11:40
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public interface Automated<T extends Serializable> {

    T populate();

    /**
     * Default realization throws an exception cause not every entity might have relations (one-to-one, one-to-many etc.)
     */
    default T populateCascade() {
        throw new UnsupportedOperationException();
    }

    T modify();

    /**
     * Default realization throws an exception cause not every entity might have relations (one-to-one, one-to-many etc.)
     */
    default T modifyCascade() {
        throw new UnsupportedOperationException();
    }

    /**
     * Method created for unit-tests to simplify entity id increment operation, to test
     * cases of loading/getting entity by id, which is not exists in database yet.
     */
    Serializable incrementIdAndGet();

    default String newValue(Serializable id, RandomString newValuePart) {
        return id + Constants.UPDATE_PREFIX + newValuePart.get();
    }

    default String newCascadeValue(Serializable id, RandomString newValuePart) {
        return id + Constants.UPDATE_CASCADE_PREFIX + newValuePart.get();
    }
}
