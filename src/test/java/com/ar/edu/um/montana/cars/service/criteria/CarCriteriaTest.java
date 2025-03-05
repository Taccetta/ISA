package com.ar.edu.um.montana.cars.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CarCriteriaTest {

    @Test
    void newCarCriteriaHasAllFiltersNullTest() {
        var carCriteria = new CarCriteria();
        assertThat(carCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void carCriteriaFluentMethodsCreatesFiltersTest() {
        var carCriteria = new CarCriteria();

        setAllFilters(carCriteria);

        assertThat(carCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void carCriteriaCopyCreatesNullFilterTest() {
        var carCriteria = new CarCriteria();
        var copy = carCriteria.copy();

        assertThat(carCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(carCriteria)
        );
    }

    @Test
    void carCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var carCriteria = new CarCriteria();
        setAllFilters(carCriteria);

        var copy = carCriteria.copy();

        assertThat(carCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(carCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var carCriteria = new CarCriteria();

        assertThat(carCriteria).hasToString("CarCriteria{}");
    }

    private static void setAllFilters(CarCriteria carCriteria) {
        carCriteria.id();
        carCriteria.model();
        carCriteria.year();
        carCriteria.available();
        carCriteria.manufacturerId();
        carCriteria.purchasedCarId();
        carCriteria.distinct();
    }

    private static Condition<CarCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getModel()) &&
                condition.apply(criteria.getYear()) &&
                condition.apply(criteria.getAvailable()) &&
                condition.apply(criteria.getManufacturerId()) &&
                condition.apply(criteria.getPurchasedCarId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CarCriteria> copyFiltersAre(CarCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getModel(), copy.getModel()) &&
                condition.apply(criteria.getYear(), copy.getYear()) &&
                condition.apply(criteria.getAvailable(), copy.getAvailable()) &&
                condition.apply(criteria.getManufacturerId(), copy.getManufacturerId()) &&
                condition.apply(criteria.getPurchasedCarId(), copy.getPurchasedCarId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
