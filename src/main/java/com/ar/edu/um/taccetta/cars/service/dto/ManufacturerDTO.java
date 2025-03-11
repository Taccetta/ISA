package com.ar.edu.um.taccetta.cars.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ar.edu.um.taccetta.cars.domain.Manufacturer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManufacturerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManufacturerDTO)) {
            return false;
        }

        ManufacturerDTO manufacturerDTO = (ManufacturerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, manufacturerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManufacturerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
