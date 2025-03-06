package com.ar.edu.um.taccetta.cars.domain;

import static com.ar.edu.um.taccetta.cars.domain.CarTestSamples.*;
import static com.ar.edu.um.taccetta.cars.domain.ManufacturerTestSamples.*;
import static com.ar.edu.um.taccetta.cars.domain.PurchasedCarTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ar.edu.um.taccetta.cars.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Car.class);
        Car car1 = getCarSample1();
        Car car2 = new Car();
        assertThat(car1).isNotEqualTo(car2);

        car2.setId(car1.getId());
        assertThat(car1).isEqualTo(car2);

        car2 = getCarSample2();
        assertThat(car1).isNotEqualTo(car2);
    }

    @Test
    void manufacturerTest() {
        Car car = getCarRandomSampleGenerator();
        Manufacturer manufacturerBack = getManufacturerRandomSampleGenerator();

        car.setManufacturer(manufacturerBack);
        assertThat(car.getManufacturer()).isEqualTo(manufacturerBack);

        car.manufacturer(null);
        assertThat(car.getManufacturer()).isNull();
    }

    @Test
    void purchasedCarTest() {
        Car car = getCarRandomSampleGenerator();
        PurchasedCar purchasedCarBack = getPurchasedCarRandomSampleGenerator();

        car.setPurchasedCar(purchasedCarBack);
        assertThat(car.getPurchasedCar()).isEqualTo(purchasedCarBack);
        assertThat(purchasedCarBack.getCar()).isEqualTo(car);

        car.purchasedCar(null);
        assertThat(car.getPurchasedCar()).isNull();
        assertThat(purchasedCarBack.getCar()).isNull();
    }
}
