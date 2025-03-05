package com.ar.edu.um.montana.cars.service.mapper;

import static com.ar.edu.um.montana.cars.domain.ManufacturerAsserts.*;
import static com.ar.edu.um.montana.cars.domain.ManufacturerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManufacturerMapperTest {

    private ManufacturerMapper manufacturerMapper;

    @BeforeEach
    void setUp() {
        manufacturerMapper = new ManufacturerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getManufacturerSample1();
        var actual = manufacturerMapper.toEntity(manufacturerMapper.toDto(expected));
        assertManufacturerAllPropertiesEquals(expected, actual);
    }
}
