package com.ar.edu.um.taccetta.cars.service.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.ar.edu.um.taccetta.cars.domain.Car;
import com.ar.edu.um.taccetta.cars.domain.Client;
import com.ar.edu.um.taccetta.cars.domain.Manufacturer;
import com.ar.edu.um.taccetta.cars.domain.PurchasedCar;
import com.ar.edu.um.taccetta.cars.service.dto.PurchasedCarDTO;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchasedCarMapperTest {

    private PurchasedCarMapper purchasedCarMapper;

    @BeforeEach
    public void setUp() {
        purchasedCarMapper = new PurchasedCarMapperImpl();
    }

    @Test
    public void testPurchaseDtoToEntity() {
        PurchasedCarDTO purchasedCarDTO = new PurchasedCarDTO();

        purchasedCarDTO.setId(1L);
        purchasedCarDTO.setPurchaseDate(LocalDate.now());

        PurchasedCar purchasedCar = purchasedCarMapper.toEntity(purchasedCarDTO);

        System.out.println(purchasedCar);

        assertEquals(purchasedCar.getId(), purchasedCarDTO.getId());
        assertEquals(purchasedCar.getPurchaseDate(), purchasedCarDTO.getPurchaseDate());
    }

    @Test
    public void testPurchaseEntityToDto() {
        PurchasedCar purchasedCar = new PurchasedCar();

        purchasedCar.setId(2L);
        purchasedCar.setPurchaseDate(LocalDate.now());
        purchasedCar.setCar(
            new Car().id(2L).model("Tesla Model S").year("2021").manufacturer(new Manufacturer().id(2L).name("Tesla")).available(1)
        );
        purchasedCar.setClient(
            new Client()
                .id(2L)
                .firstName("Jane")
                .lastName("Doe")
                .address("Avenida Montenegro 123")
                .email("jane.doe@gmail.com")
                .phone("1234567890")
        );

        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        System.out.println(purchasedCarDTO);

        assertEquals(purchasedCar.getId(), purchasedCarDTO.getId());
        assertEquals(purchasedCar.getPurchaseDate(), purchasedCarDTO.getPurchaseDate());
        assertEquals(purchasedCar.getCar().getId(), purchasedCarDTO.getCar().getId());
        assertEquals(purchasedCar.getClient().getId(), purchasedCarDTO.getClient().getId());
    }

    @Test
    public void testNullPurchaseDtoToEntity() {
        PurchasedCarDTO purchasedCarDTO = new PurchasedCarDTO();

        purchasedCarDTO.setId(null);
        purchasedCarDTO.setPurchaseDate(null);

        PurchasedCar purchasedCar = purchasedCarMapper.toEntity(purchasedCarDTO);

        System.out.println(purchasedCar);

        assertNull(purchasedCar.getId());
        assertNull(purchasedCar.getPurchaseDate());
    }
}
