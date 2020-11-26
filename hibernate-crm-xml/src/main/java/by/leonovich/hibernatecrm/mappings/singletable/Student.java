package by.leonovich.hibernatecrm.mappings.singletable;

import lombok.Data;

/**
 * Created : 26/11/2020 21:37
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class Student extends Person {

    private String faculty;
    private Double mark;
}
