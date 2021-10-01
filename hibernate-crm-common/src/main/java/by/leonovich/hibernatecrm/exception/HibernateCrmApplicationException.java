package by.leonovich.hibernatecrm.exception;

/**
 * Created : 30/09/2021 15:06
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class HibernateCrmApplicationException extends RuntimeException {

    public HibernateCrmApplicationException(Exception e) {
        super(e);
    }
}
