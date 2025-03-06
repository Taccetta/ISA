package com.ar.edu.um.taccetta.cars.service.mapper;

import com.ar.edu.um.taccetta.cars.domain.Car;
import com.ar.edu.um.taccetta.cars.domain.Client;
import com.ar.edu.um.taccetta.cars.domain.PurchasedCar;
import com.ar.edu.um.taccetta.cars.service.dto.CarDTO;
import com.ar.edu.um.taccetta.cars.service.dto.ClientDTO;
import com.ar.edu.um.taccetta.cars.service.dto.PurchasedCarDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchasedCar} and its DTO {@link PurchasedCarDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchasedCarMapper extends EntityMapper<PurchasedCarDTO, PurchasedCar> {
    @Mapping(target = "car", source = "car", qualifiedByName = "carId")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    PurchasedCarDTO toDto(PurchasedCar s);

    @Named("carId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CarDTO toDtoCarId(Car car);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);
}
