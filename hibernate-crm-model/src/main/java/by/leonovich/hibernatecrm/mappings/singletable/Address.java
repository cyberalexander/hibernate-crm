package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.common.random.RandomCountry;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created : 13/12/2020 15:58
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class Address implements Serializable {
    private String country;
    private String countryCode;
    private String city;
    private String street;
    private Integer buildingNumber;
    @EqualsAndHashCode.Exclude
    private String postIndex;

    @SuppressWarnings("unchecked")
    public <T extends Address> T populate() {
        Locale randCountry = new RandomCountry().get();
        this.setCountry(randCountry.getDisplayCountry());
        this.setCountryCode(randCountry.getCountry());
        this.setCity(RandomString.DEFAULT.get());
        this.setStreet(RandomString.DEFAULT.get());
        this.setBuildingNumber(RandomNumber.DEFAULT_I.get());
        return (T) this;
    }

    public static Address init() {
        return new Address().populate();
    }
}
