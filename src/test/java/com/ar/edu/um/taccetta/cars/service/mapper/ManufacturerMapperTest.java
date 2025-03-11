package com.ar.edu.um.taccetta.cars.service.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.ar.edu.um.taccetta.cars.domain.Manufacturer;
import com.ar.edu.um.taccetta.cars.service.dto.ManufacturerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManufacturerMapperTest {

    private ManufacturerMapper manufacturerMapper;

    @BeforeEach
    public void setUp() {
        manufacturerMapper = new ManufacturerMapperImpl();
    }

    @Test
    public void testManDtoToEntity() {
        ManufacturerDTO manufacturerDTO = new ManufacturerDTO();
        manufacturerDTO.setId(1L);
        manufacturerDTO.setName("Volkswagen");

        Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDTO);

        System.out.println(manufacturer);

        assertEquals(manufacturer.getId(), manufacturerDTO.getId());
        assertEquals(manufacturer.getName(), manufacturerDTO.getName());
    }

    @Test
    public void testManEntityToDto() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(2L);
        manufacturer.setName("Tesla");

        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);

        System.out.println(manufacturerDTO);

        assertEquals(manufacturer.getId(), manufacturerDTO.getId());
        assertEquals(manufacturer.getName(), manufacturerDTO.getName());
    }

    @Test
    public void testNullManDtoToEntity() {
        ManufacturerDTO manufacturerDTO = new ManufacturerDTO();
        manufacturerDTO.setId(null);
        manufacturerDTO.setName(null);

        Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDTO);

        System.out.println(manufacturer);

        assertNull(manufacturer.getId());
        assertNull(manufacturer.getName());
    }
}
