package by.leonovich.hibernatecrm.dao;

import lombok.Getter;
import lombok.Setter;

/**
 * Created : 26/11/2020 21:59
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class DaoException extends Exception {

    @Getter
    @Setter
    private Exception exception;

    public DaoException(Exception exception) {
        super(exception);
        this.exception = exception;
    }
}
