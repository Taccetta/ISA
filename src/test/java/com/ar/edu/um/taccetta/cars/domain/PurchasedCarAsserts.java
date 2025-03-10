package com.ar.edu.um.taccetta.cars.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PurchasedCarAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPurchasedCarAllPropertiesEquals(PurchasedCar expected, PurchasedCar actual) {
        assertPurchasedCarAutoGeneratedPropertiesEquals(expected, actual);
        assertPurchasedCarAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPurchasedCarAllUpdatablePropertiesEquals(PurchasedCar expected, PurchasedCar actual) {
        assertPurchasedCarUpdatableFieldsEquals(expected, actual);
        assertPurchasedCarUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPurchasedCarAutoGeneratedPropertiesEquals(PurchasedCar expected, PurchasedCar actual) {
        assertThat(actual)
            .as("Verify PurchasedCar auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPurchasedCarUpdatableFieldsEquals(PurchasedCar expected, PurchasedCar actual) {
        assertThat(actual)
            .as("Verify PurchasedCar relevant properties")
            .satisfies(a -> assertThat(a.getPurchaseDate()).as("check purchaseDate").isEqualTo(expected.getPurchaseDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPurchasedCarUpdatableRelationshipsEquals(PurchasedCar expected, PurchasedCar actual) {
        assertThat(actual)
            .as("Verify PurchasedCar relationships")
            .satisfies(a -> assertThat(a.getCar()).as("check car").isEqualTo(expected.getCar()))
            .satisfies(a -> assertThat(a.getClient()).as("check client").isEqualTo(expected.getClient()));
    }
}
