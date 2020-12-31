package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.mappings.Automated;
import by.leonovich.hibernatecrm.tools.RandomNumber;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created : 26/12/2020 12:06
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class Meeting implements Serializable, Automated<Meeting> {
    private Long id;
    private String subject;
    private LocalDateTime meetingDate;
    @EqualsAndHashCode.Exclude
    private Boolean expired;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Employee> employees = new HashSet<>(); /* MANY-TO-MANY relation */

    @Override
    public Meeting populate() {
        this.setSubject(RandomString.DEFAULT.get());
        if (new Random().nextBoolean()) {
            this.setMeetingDate(
                LocalDateTime.now().plusDays(Long.parseLong(RandomNumber.DEFAULT_I.get().toString()))
            );
        } else {
            this.setMeetingDate(
                LocalDateTime.now().minusDays(Long.parseLong(RandomNumber.DEFAULT_I.get().toString()))
            );
        }
        return this;
    }

    @Override
    public Meeting populateCascade() {
        this.populate();
        this.setEmployees(Stream.generate(Employee::init).limit(3).collect(Collectors.toSet()));
        this.getEmployees().forEach(emp -> emp.setMeetings(Collections.singleton(this)));
        return this;
    }

    @Override
    public Meeting modify() {
        this.setSubject(newValue(this.getId(), RandomString.DEFAULT));
        return this;
    }

    @Override
    public Meeting modifyCascade() {
        this.modify();
        this.getEmployees().forEach(emp -> emp.setCompany(newCascadeValue(this.getId(), RandomString.COMPANY)));
        return this;
    }

    @Override
    public Serializable incrementIdAndGet() {
        return this.getId() + 500L;
    }

    public static Meeting init() {
        return new Meeting().populate();
    }

    public static Meeting initWithManyToMany() {
        return new Meeting().populateCascade();
    }
}
