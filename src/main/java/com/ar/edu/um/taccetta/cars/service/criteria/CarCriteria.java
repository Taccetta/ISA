package com.ar.edu.um.taccetta.cars.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ar.edu.um.taccetta.cars.domain.Car} entity. This class is used
 * in {@link com.ar.edu.um.taccetta.cars.web.rest.CarResource} to receive all the possible filtering options from
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

    private Boolean distinct;

    public CarCriteria() {}

    public CarCriteria(CarCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.year = other.year == null ? null : other.year.copy();
        this.available = other.available == null ? null : other.available.copy();
        this.manufacturerId = other.manufacturerId == null ? null : other.manufacturerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CarCriteria copy() {
        return new CarCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getModel() {
        return model;
    }

    public StringFilter model() {
        if (model == null) {
            model = new StringFilter();
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public StringFilter getYear() {
        return year;
    }

    public StringFilter year() {
        if (year == null) {
            year = new StringFilter();
        }
        return year;
    }

    public void setYear(StringFilter year) {
        this.year = year;
    }

    public IntegerFilter getAvailable() {
        return available;
    }

    public IntegerFilter available() {
        if (available == null) {
            available = new IntegerFilter();
        }
        return available;
    }

    public void setAvailable(IntegerFilter available) {
        this.available = available;
    }

    public LongFilter getManufacturerId() {
        return manufacturerId;
    }

    public LongFilter manufacturerId() {
        if (manufacturerId == null) {
            manufacturerId = new LongFilter();
        }
        return manufacturerId;
    }

    public void setManufacturerId(LongFilter manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Boolean getDistinct() {
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
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, year, available, manufacturerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (year != null ? "year=" + year + ", " : "") +
            (available != null ? "available=" + available + ", " : "") +
            (manufacturerId != null ? "manufacturerId=" + manufacturerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
