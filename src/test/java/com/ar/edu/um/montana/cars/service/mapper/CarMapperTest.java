package com.ar.edu.um.montana.cars.service.mapper;

import static com.ar.edu.um.montana.cars.domain.CarAsserts.*;
import static com.ar.edu.um.montana.cars.domain.CarTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarMapperTest {

    private CarMapper carMapper;

    @BeforeEach
    void setUp() {
        carMapper = new CarMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCarSample1();
        var actual = carMapper.toEntity(carMapper.toDto(expected));
        assertCarAllPropertiesEquals(expected, actual);
    }
}
