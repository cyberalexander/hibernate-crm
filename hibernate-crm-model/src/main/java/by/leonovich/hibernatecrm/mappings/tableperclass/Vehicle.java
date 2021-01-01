package by.leonovich.hibernatecrm.mappings.tableperclass;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;

import java.io.Serializable;

/**
 * Created : 10/12/2020 22:52
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class Vehicle implements Serializable, Automated<Vehicle> {
    private Long id;
    private Double engineVolume;
    private String manufacturer;

    @Override
    public Vehicle populate() {
        this.setEngineVolume(RandomNumber.ENGINE_VOLUME.get());
        this.setManufacturer(RandomString.MANUFACTURER.get());
        return this;
    }

    @Override
    public Vehicle modify() {
        this.setManufacturer(newValue(this.getId(), RandomString.MANUFACTURER));
        return this;
    }

    @Override
    public Serializable incrementIdAndGet() {
        return this.getId() + 500L;
    }

    public static Vehicle init() {
        return new Vehicle().populate();
    }
}
