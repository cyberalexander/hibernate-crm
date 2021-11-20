package by.leonovich.hibernatecrm.exception;

import java.io.Serial;

/**
 * Created : 30/09/2021 15:06
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class HibernateCrmApplicationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 194364542624258692L;

    public HibernateCrmApplicationException(final Exception e) {
        super(e);
    }
}
