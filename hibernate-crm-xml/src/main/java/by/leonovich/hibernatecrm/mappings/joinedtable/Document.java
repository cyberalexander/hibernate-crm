package by.leonovich.hibernatecrm.mappings.joinedtable;

import by.leonovich.hibernatecrm.mappings.Automated;
import by.leonovich.hibernatecrm.tools.RandomNumber;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.Data;

import java.io.Serializable;
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
public class Document implements Serializable, Automated<Document> {
    private Long id;
    private String documentNumber;
    private LocalDate issueDate;
    private LocalDate expirationDate;

    @Override
    public Document populate() {
        this.setDocumentNumber(RandomString.DOCUMENT_NUMBER.get());
        this.setIssueDate(LocalDate.now().minusDays(RandomNumber.DAYS.get()));
        this.setExpirationDate(LocalDate.now().plusDays(RandomNumber.DAYS.get()));
        return this;
    }

    @Override
    public Document modify() {
        this.setDocumentNumber(newValue(this.getId(), RandomString.DOCUMENT_NUMBER));
        return this;
    }

    @Override
    public Serializable incrementIdAndGet() {
        return this.getId() + 500L;
    }

    public static Document init() {
        return new Document().populate();
    }
}
