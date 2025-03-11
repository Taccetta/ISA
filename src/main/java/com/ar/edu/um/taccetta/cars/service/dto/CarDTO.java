package com.ar.edu.um.taccetta.cars.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ar.edu.um.taccetta.cars.domain.Car} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String model;

    @NotNull
    @Size(min = 4, max = 50)
    private String year;

    @NotNull
    private Integer available;

    private ManufacturerDTO manufacturer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public ManufacturerDTO getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerDTO manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarDTO)) {
            return false;
        }

        CarDTO carDTO = (CarDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarDTO{" +
            "id=" + getId() +
            ", model='" + getModel() + "'" +
            ", year='" + getYear() + "'" +
            ", available=" + getAvailable() +
            ", manufacturer=" + getManufacturer() +
            "}";
    }
}
