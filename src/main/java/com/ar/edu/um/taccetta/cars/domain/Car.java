package com.ar.edu.um.taccetta.cars.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "model", length = 100, nullable = false)
    private String model;

    @NotNull
    @Size(min = 4, max = 50)
    @Column(name = "year", length = 50, nullable = false)
    private String year;

    @NotNull
    @Column(name = "available", nullable = false)
    private Integer available;

    @OneToOne
    @JoinColumn(unique = true)
    private Manufacturer manufacturer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Car id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return this.model;
    }

    public Car model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return this.year;
    }

    public Car year(String year) {
        this.setYear(year);
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getAvailable() {
        return this.available;
    }

    public Car available(Integer available) {
        this.setAvailable(available);
        return this;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Manufacturer getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Car manufacturer(Manufacturer manufacturer) {
        this.setManufacturer(manufacturer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return id != null && id.equals(((Car) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", model='" + getModel() + "'" +
            ", year='" + getYear() + "'" +
            ", available=" + getAvailable() +
            "}";
    }
}
