package com.ar.edu.um.taccetta.cars.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PurchasedCarCriteriaTest {

    @Test
    void newPurchasedCarCriteriaHasAllFiltersNullTest() {
        var purchasedCarCriteria = new PurchasedCarCriteria();
        assertThat(purchasedCarCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void purchasedCarCriteriaFluentMethodsCreatesFiltersTest() {
        var purchasedCarCriteria = new PurchasedCarCriteria();

        setAllFilters(purchasedCarCriteria);

        assertThat(purchasedCarCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void purchasedCarCriteriaCopyCreatesNullFilterTest() {
        var purchasedCarCriteria = new PurchasedCarCriteria();
        var copy = purchasedCarCriteria.copy();

        assertThat(purchasedCarCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(purchasedCarCriteria)
        );
    }

    @Test
    void purchasedCarCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var purchasedCarCriteria = new PurchasedCarCriteria();
        setAllFilters(purchasedCarCriteria);

        var copy = purchasedCarCriteria.copy();

        assertThat(purchasedCarCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(purchasedCarCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var purchasedCarCriteria = new PurchasedCarCriteria();

        assertThat(purchasedCarCriteria).hasToString("PurchasedCarCriteria{}");
    }

    private static void setAllFilters(PurchasedCarCriteria purchasedCarCriteria) {
        purchasedCarCriteria.id();
        purchasedCarCriteria.purchaseDate();
        purchasedCarCriteria.carId();
        purchasedCarCriteria.clientId();
        purchasedCarCriteria.distinct();
    }

    private static Condition<PurchasedCarCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPurchaseDate()) &&
                condition.apply(criteria.getCarId()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PurchasedCarCriteria> copyFiltersAre(
        PurchasedCarCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPurchaseDate(), copy.getPurchaseDate()) &&
                condition.apply(criteria.getCarId(), copy.getCarId()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
