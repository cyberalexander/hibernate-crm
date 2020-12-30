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

    T populateCascade();

    T modify();

    T modifyCascade();

    default String newValue(Serializable id, RandomString newValuePart) {
        return id + Constants.UPDATE_PREFIX + newValuePart.get();
    }

    default String newCascadeValue(Serializable id, RandomString newValuePart) {
        return id + Constants.UPDATE_CASCADE_PREFIX + newValuePart.get();
    }
}
