package com.ar.edu.um.taccetta.cars.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ar.edu.um.taccetta.cars.domain.PurchasedCar} entity. This class is used
 * in {@link com.ar.edu.um.taccetta.cars.web.rest.PurchasedCarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /purchased-cars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchasedCarCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter purchaseDate;

    private LongFilter carId;

    private LongFilter clientId;

    private Boolean distinct;

    public PurchasedCarCriteria() {}

    public PurchasedCarCriteria(PurchasedCarCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.purchaseDate = other.optionalPurchaseDate().map(LocalDateFilter::copy).orElse(null);
        this.carId = other.optionalCarId().map(LongFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PurchasedCarCriteria copy() {
        return new PurchasedCarCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getPurchaseDate() {
        return purchaseDate;
    }

    public Optional<LocalDateFilter> optionalPurchaseDate() {
        return Optional.ofNullable(purchaseDate);
    }

    public LocalDateFilter purchaseDate() {
        if (purchaseDate == null) {
            setPurchaseDate(new LocalDateFilter());
        }
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateFilter purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LongFilter getCarId() {
        return carId;
    }

    public Optional<LongFilter> optionalCarId() {
        return Optional.ofNullable(carId);
    }

    public LongFilter carId() {
        if (carId == null) {
            setCarId(new LongFilter());
        }
        return carId;
    }

    public void setCarId(LongFilter carId) {
        this.carId = carId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public Optional<LongFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public LongFilter clientId() {
        if (clientId == null) {
            setClientId(new LongFilter());
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PurchasedCarCriteria that = (PurchasedCarCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(purchaseDate, that.purchaseDate) &&
            Objects.equals(carId, that.carId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, purchaseDate, carId, clientId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchasedCarCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPurchaseDate().map(f -> "purchaseDate=" + f + ", ").orElse("") +
            optionalCarId().map(f -> "carId=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
