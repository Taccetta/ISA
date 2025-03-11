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
    @Mapping(target = "car", source = "car", qualifiedByName = "carModel")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientEmail")
    PurchasedCarDTO toDto(PurchasedCar s);

    @Named("carModel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "model", source = "model")
    CarDTO toDtoCarModel(Car car);

    @Named("clientEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    ClientDTO toDtoClientEmail(Client client);
}
