package by.leonovich.hibernatecrm.mappings.joinedtable;

import by.leonovich.hibernatecrm.tools.RandomNumber;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.Data;

import java.time.LocalDate;

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
    private LocalDate issueDate;
    private LocalDate expirationDate;

    public <T extends Document> T populate() {
        this.setDocumentNumber(RandomString.DOCUMENT_NUMBER.get());
        this.setIssueDate(LocalDate.now().minusDays(RandomNumber.DAYS.get()));
        this.setExpirationDate(LocalDate.now().plusDays(RandomNumber.DAYS.get()));
        return (T) this;
    }

    public static Document init() {
        return new Document().populate();
    }
}
