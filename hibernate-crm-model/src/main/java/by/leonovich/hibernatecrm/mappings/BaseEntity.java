package by.leonovich.hibernatecrm.mappings;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created : 02/01/2021 16:07
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
@EqualsAndHashCode
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class BaseEntity {
    private Long id;
}
