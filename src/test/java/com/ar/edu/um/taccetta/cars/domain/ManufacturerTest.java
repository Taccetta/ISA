package com.ar.edu.um.taccetta.cars.domain;

import static com.ar.edu.um.taccetta.cars.domain.CarTestSamples.*;
import static com.ar.edu.um.taccetta.cars.domain.ManufacturerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ar.edu.um.taccetta.cars.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManufacturerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manufacturer.class);
        Manufacturer manufacturer1 = getManufacturerSample1();
        Manufacturer manufacturer2 = new Manufacturer();
        assertThat(manufacturer1).isNotEqualTo(manufacturer2);

        manufacturer2.setId(manufacturer1.getId());
        assertThat(manufacturer1).isEqualTo(manufacturer2);

        manufacturer2 = getManufacturerSample2();
        assertThat(manufacturer1).isNotEqualTo(manufacturer2);
    }

    @Test
    void carTest() {
        Manufacturer manufacturer = getManufacturerRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        manufacturer.setCar(carBack);
        assertThat(manufacturer.getCar()).isEqualTo(carBack);
        assertThat(carBack.getManufacturer()).isEqualTo(manufacturer);

        manufacturer.car(null);
        assertThat(manufacturer.getCar()).isNull();
        assertThat(carBack.getManufacturer()).isNull();
    }
}
