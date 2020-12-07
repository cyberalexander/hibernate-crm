package by.leonovich.hibernatecrm.mappings.joinedtable;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created : 06/12/2020 18:13
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class Document {
    private Long id;
    private String documentNumber;
    private LocalDateTime issueDate;
    private LocalDateTime expirationDate;
}
