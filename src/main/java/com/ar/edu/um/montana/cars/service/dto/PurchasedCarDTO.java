package com.ar.edu.um.montana.cars.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.ar.edu.um.montana.cars.domain.PurchasedCar} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchasedCarDTO implements Serializable {

    private Long id;

    private LocalDate purchaseDate;

    private CarDTO car;

    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchasedCarDTO)) {
            return false;
        }

        PurchasedCarDTO purchasedCarDTO = (PurchasedCarDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, purchasedCarDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchasedCarDTO{" +
            "id=" + getId() +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", car=" + getCar() +
            ", client=" + getClient() +
            "}";
    }
}
