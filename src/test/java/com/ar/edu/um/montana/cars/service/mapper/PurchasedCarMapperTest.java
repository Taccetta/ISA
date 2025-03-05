package com.ar.edu.um.montana.cars.service.mapper;

import static com.ar.edu.um.montana.cars.domain.PurchasedCarAsserts.*;
import static com.ar.edu.um.montana.cars.domain.PurchasedCarTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchasedCarMapperTest {

    private PurchasedCarMapper purchasedCarMapper;

    @BeforeEach
    void setUp() {
        purchasedCarMapper = new PurchasedCarMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPurchasedCarSample1();
        var actual = purchasedCarMapper.toEntity(purchasedCarMapper.toDto(expected));
        assertPurchasedCarAllPropertiesEquals(expected, actual);
    }
}
