package com.ar.edu.um.taccetta.cars.service.mapper;

import com.ar.edu.um.taccetta.cars.domain.Car;
import com.ar.edu.um.taccetta.cars.domain.Manufacturer;
import com.ar.edu.um.taccetta.cars.service.dto.CarDTO;
import com.ar.edu.um.taccetta.cars.service.dto.ManufacturerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Car} and its DTO {@link CarDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarMapper extends EntityMapper<CarDTO, Car> {
    @Mapping(target = "manufacturer", source = "manufacturer", qualifiedByName = "manufacturerName")
    CarDTO toDto(Car s);

    @Named("manufacturerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ManufacturerDTO toDtoManufacturerName(Manufacturer manufacturer);
}
