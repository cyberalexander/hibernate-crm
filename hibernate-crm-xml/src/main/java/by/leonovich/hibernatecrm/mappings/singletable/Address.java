package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.tools.RandomCountry;
import by.leonovich.hibernatecrm.tools.RandomNumber;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class Address {
    private String country;
    private String countryCode;
    private String city;
    private String street;
    private Integer buildingNumber;
    @EqualsAndHashCode.Exclude
    private String index;

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
