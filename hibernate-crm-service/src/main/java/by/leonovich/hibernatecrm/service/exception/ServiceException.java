package by.leonovich.hibernatecrm.service.exception;

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
public class ServiceException extends Exception {

    @Getter
    @Setter
    private Exception exception;

    public ServiceException(Exception exception) {
        super(exception);
        this.exception = exception;
    }
}
