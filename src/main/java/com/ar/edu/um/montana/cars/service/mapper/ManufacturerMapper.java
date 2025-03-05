package com.ar.edu.um.montana.cars.service.mapper;

import com.ar.edu.um.montana.cars.domain.Manufacturer;
import com.ar.edu.um.montana.cars.service.dto.ManufacturerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Manufacturer} and its DTO {@link ManufacturerDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManufacturerMapper extends EntityMapper<ManufacturerDTO, Manufacturer> {}
