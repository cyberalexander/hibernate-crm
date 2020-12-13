package by.leonovich.hibernatecrm.mappings.tableperclass;

import by.leonovich.hibernatecrm.tools.RandomNumber;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.Data;

/**
 * Created : 10/12/2020 22:52
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class Vehicle {
    private Long id;
    private Double engineVolume;
    private String manufacturer;

    public <T extends Vehicle> T populate() {
        this.setEngineVolume(RandomNumber.ENGINE_VOLUME.get());
        this.setManufacturer(RandomString.MANUFACTURER.get());
        return (T) this;
    }

    public static Vehicle init() {
        return new Vehicle().populate();
    }
}
