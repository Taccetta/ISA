package com.ar.edu.um.taccetta.cars.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ar.edu.um.taccetta.cars.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedCarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedCar.class);
        PurchasedCar purchasedCar1 = new PurchasedCar();
        purchasedCar1.setId(1L);
        PurchasedCar purchasedCar2 = new PurchasedCar();
        purchasedCar2.setId(purchasedCar1.getId());
        assertThat(purchasedCar1).isEqualTo(purchasedCar2);
        purchasedCar2.setId(2L);
        assertThat(purchasedCar1).isNotEqualTo(purchasedCar2);
        purchasedCar1.setId(null);
        assertThat(purchasedCar1).isNotEqualTo(purchasedCar2);
    }
}
