package com.ar.edu.um.montana.cars.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ManufacturerCriteriaTest {

    @Test
    void newManufacturerCriteriaHasAllFiltersNullTest() {
        var manufacturerCriteria = new ManufacturerCriteria();
        assertThat(manufacturerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void manufacturerCriteriaFluentMethodsCreatesFiltersTest() {
        var manufacturerCriteria = new ManufacturerCriteria();

        setAllFilters(manufacturerCriteria);

        assertThat(manufacturerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void manufacturerCriteriaCopyCreatesNullFilterTest() {
        var manufacturerCriteria = new ManufacturerCriteria();
        var copy = manufacturerCriteria.copy();

        assertThat(manufacturerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(manufacturerCriteria)
        );
    }

    @Test
    void manufacturerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var manufacturerCriteria = new ManufacturerCriteria();
        setAllFilters(manufacturerCriteria);

        var copy = manufacturerCriteria.copy();

        assertThat(manufacturerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(manufacturerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var manufacturerCriteria = new ManufacturerCriteria();

        assertThat(manufacturerCriteria).hasToString("ManufacturerCriteria{}");
    }

    private static void setAllFilters(ManufacturerCriteria manufacturerCriteria) {
        manufacturerCriteria.id();
        manufacturerCriteria.name();
        manufacturerCriteria.carId();
        manufacturerCriteria.distinct();
    }

    private static Condition<ManufacturerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCarId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ManufacturerCriteria> copyFiltersAre(
        ManufacturerCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCarId(), copy.getCarId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
