package com.ar.edu.um.montana.cars.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Manufacturer.
 */
@Entity
@Table(name = "manufacturer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Manufacturer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @JsonIgnoreProperties(value = { "manufacturer", "purchasedCar" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "manufacturer")
    private Car car;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Manufacturer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Manufacturer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        if (this.car != null) {
            this.car.setManufacturer(null);
        }
        if (car != null) {
            car.setManufacturer(this);
        }
        this.car = car;
    }

    public Manufacturer car(Car car) {
        this.setCar(car);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Manufacturer)) {
            return false;
        }
        return getId() != null && getId().equals(((Manufacturer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Manufacturer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
