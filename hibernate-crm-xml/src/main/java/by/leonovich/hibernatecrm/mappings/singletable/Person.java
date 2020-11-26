package by.leonovich.hibernatecrm.mappings.singletable;

import lombok.Data;

import java.io.Serializable;

/**
 * Created : 26/11/2020 21:28
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class Person implements Serializable {

    private Long id;
    private String name;
    private String surname;
    private Integer age;
}
