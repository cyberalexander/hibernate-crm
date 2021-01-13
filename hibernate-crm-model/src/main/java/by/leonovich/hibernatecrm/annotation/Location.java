package by.leonovich.hibernatecrm.annotation;

import by.leonovich.hibernatecrm.common.random.RandomCountry;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Locale;

/**
 * Created : 07/01/2021 10:20
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
@Embeddable
public class Location implements Serializable {
    @Column(name = "F_COUNTRY")
    private String country;

    @Column(name = "F_CITY")
    private String city;

    @Column(name = "F_SREET")
    private String street;

    @Column(name = "F_BUILDING")
    private Integer buildingNumber;

    @SuppressWarnings("unchecked")
    public <T extends Location> T populate() {
        Locale randCountry = new RandomCountry().get();
        this.setCountry(randCountry.getDisplayCountry());
        this.setCity(RandomString.DEFAULT.get());
        this.setStreet(RandomString.DEFAULT.get());
        this.setBuildingNumber(RandomNumber.DEFAULT_I.get());
        return (T) this;
    }

    public static Location init() {
        return new Location().populate();
    }
}
