package com.ar.edu.um.taccetta.cars.domain;

import static com.ar.edu.um.taccetta.cars.domain.CarTestSamples.*;
import static com.ar.edu.um.taccetta.cars.domain.ClientTestSamples.*;
import static com.ar.edu.um.taccetta.cars.domain.PurchasedCarTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ar.edu.um.taccetta.cars.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedCarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedCar.class);
        PurchasedCar purchasedCar1 = getPurchasedCarSample1();
        PurchasedCar purchasedCar2 = new PurchasedCar();
        assertThat(purchasedCar1).isNotEqualTo(purchasedCar2);

        purchasedCar2.setId(purchasedCar1.getId());
        assertThat(purchasedCar1).isEqualTo(purchasedCar2);

        purchasedCar2 = getPurchasedCarSample2();
        assertThat(purchasedCar1).isNotEqualTo(purchasedCar2);
    }

    @Test
    void carTest() {
        PurchasedCar purchasedCar = getPurchasedCarRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        purchasedCar.setCar(carBack);
        assertThat(purchasedCar.getCar()).isEqualTo(carBack);

        purchasedCar.car(null);
        assertThat(purchasedCar.getCar()).isNull();
    }

    @Test
    void clientTest() {
        PurchasedCar purchasedCar = getPurchasedCarRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        purchasedCar.setClient(clientBack);
        assertThat(purchasedCar.getClient()).isEqualTo(clientBack);

        purchasedCar.client(null);
        assertThat(purchasedCar.getClient()).isNull();
    }
}
