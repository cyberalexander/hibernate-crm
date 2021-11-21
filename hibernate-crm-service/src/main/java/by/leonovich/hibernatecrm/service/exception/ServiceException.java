package by.leonovich.hibernatecrm.service.exception;

import lombok.Getter;
import java.io.Serial;

/**
 * Created : 26/11/2020 21:59
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class ServiceException extends Exception {
    @Serial
    private static final long serialVersionUID = 9126351143499635053L;

    @Getter
    private final Exception exception;

    public ServiceException(Exception exception) {
        super(exception);
        this.exception = exception;
    }
}
