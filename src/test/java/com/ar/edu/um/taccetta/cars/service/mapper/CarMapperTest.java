package com.ar.edu.um.taccetta.cars.service.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.ar.edu.um.taccetta.cars.domain.Car;
import com.ar.edu.um.taccetta.cars.domain.Manufacturer;
import com.ar.edu.um.taccetta.cars.service.dto.CarDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarMapperTest {

    private CarMapper carMapper;

    @BeforeEach
    public void setUp() {
        carMapper = new CarMapperImpl();
    }

    @Test
    public void testCarDtoToEntity() {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(1L);
        carDTO.setModel("Volkswagen Up");
        carDTO.setYear("2018");
        carDTO.setAvailable(3);

        Car car = carMapper.toEntity(carDTO);

        System.out.println(car);

        assertEquals(car.getId(), carDTO.getId());
        assertEquals(car.getModel(), carDTO.getModel());
        assertEquals(car.getYear(), carDTO.getYear());
        assertEquals(car.getAvailable(), carDTO.getAvailable());
    }

    @Test
    public void testCarEntitytoDto() {
        Car car = new Car();
        car.setId(2L);
        car.setModel("Tesla Model S");
        car.setYear("2021");
        car.setManufacturer(new Manufacturer().id(2L).name("Tesla"));
        car.setAvailable(1);

        CarDTO carDTO = carMapper.toDto(car);

        System.out.println(carDTO);

        assertEquals(car.getId(), carDTO.getId());
        assertEquals(car.getModel(), carDTO.getModel());
        assertEquals(car.getYear(), carDTO.getYear());
        assertEquals(car.getAvailable(), carDTO.getAvailable());
        assertEquals(car.getManufacturer().getId(), carDTO.getManufacturer().getId());
    }

    @Test
    public void testNullCarDtoToEntity() {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(null);
        carDTO.setModel(null);
        carDTO.setYear(null);
        carDTO.setAvailable(null);

        Car car = carMapper.toEntity(carDTO);

        System.out.println(car);

        assertNull(car.getId());
        assertNull(car.getModel());
        assertNull(car.getYear());
        assertNull(car.getAvailable());
    }
}
