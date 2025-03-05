package com.ar.edu.um.montana.cars.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ar.edu.um.montana.cars.domain.Car} entity. This class is used
 * in {@link com.ar.edu.um.montana.cars.web.rest.CarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter model;

    private StringFilter year;

    private IntegerFilter available;

    private LongFilter manufacturerId;

    private LongFilter purchasedCarId;

    private Boolean distinct;

    public CarCriteria() {}

    public CarCriteria(CarCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.model = other.optionalModel().map(StringFilter::copy).orElse(null);
        this.year = other.optionalYear().map(StringFilter::copy).orElse(null);
        this.available = other.optionalAvailable().map(IntegerFilter::copy).orElse(null);
        this.manufacturerId = other.optionalManufacturerId().map(LongFilter::copy).orElse(null);
        this.purchasedCarId = other.optionalPurchasedCarId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CarCriteria copy() {
        return new CarCriteria(this);
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

    public StringFilter getModel() {
        return model;
    }

    public Optional<StringFilter> optionalModel() {
        return Optional.ofNullable(model);
    }

    public StringFilter model() {
        if (model == null) {
            setModel(new StringFilter());
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public StringFilter getYear() {
        return year;
    }

    public Optional<StringFilter> optionalYear() {
        return Optional.ofNullable(year);
    }

    public StringFilter year() {
        if (year == null) {
            setYear(new StringFilter());
        }
        return year;
    }

    public void setYear(StringFilter year) {
        this.year = year;
    }

    public IntegerFilter getAvailable() {
        return available;
    }

    public Optional<IntegerFilter> optionalAvailable() {
        return Optional.ofNullable(available);
    }

    public IntegerFilter available() {
        if (available == null) {
            setAvailable(new IntegerFilter());
        }
        return available;
    }

    public void setAvailable(IntegerFilter available) {
        this.available = available;
    }

    public LongFilter getManufacturerId() {
        return manufacturerId;
    }

    public Optional<LongFilter> optionalManufacturerId() {
        return Optional.ofNullable(manufacturerId);
    }

    public LongFilter manufacturerId() {
        if (manufacturerId == null) {
            setManufacturerId(new LongFilter());
        }
        return manufacturerId;
    }

    public void setManufacturerId(LongFilter manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public LongFilter getPurchasedCarId() {
        return purchasedCarId;
    }

    public Optional<LongFilter> optionalPurchasedCarId() {
        return Optional.ofNullable(purchasedCarId);
    }

    public LongFilter purchasedCarId() {
        if (purchasedCarId == null) {
            setPurchasedCarId(new LongFilter());
        }
        return purchasedCarId;
    }

    public void setPurchasedCarId(LongFilter purchasedCarId) {
        this.purchasedCarId = purchasedCarId;
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
        final CarCriteria that = (CarCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(model, that.model) &&
            Objects.equals(year, that.year) &&
            Objects.equals(available, that.available) &&
            Objects.equals(manufacturerId, that.manufacturerId) &&
            Objects.equals(purchasedCarId, that.purchasedCarId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, year, available, manufacturerId, purchasedCarId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalModel().map(f -> "model=" + f + ", ").orElse("") +
            optionalYear().map(f -> "year=" + f + ", ").orElse("") +
            optionalAvailable().map(f -> "available=" + f + ", ").orElse("") +
            optionalManufacturerId().map(f -> "manufacturerId=" + f + ", ").orElse("") +
            optionalPurchasedCarId().map(f -> "purchasedCarId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
