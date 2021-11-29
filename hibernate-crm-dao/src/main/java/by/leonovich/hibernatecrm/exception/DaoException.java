package by.leonovich.hibernatecrm.exception;

import lombok.Getter;

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
    private final Exception exception;

    public DaoException(final Exception e) {
        super(e);
        this.exception = e;
    }
}
