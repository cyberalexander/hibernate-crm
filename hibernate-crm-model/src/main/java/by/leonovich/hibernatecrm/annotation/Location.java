package by.leonovich.hibernatecrm.annotation;

import by.leonovich.hibernatecrm.common.random.RandomCountry;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;

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
public class Location implements Serializable {
    private String country;
    private String city;
    private String street;
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
