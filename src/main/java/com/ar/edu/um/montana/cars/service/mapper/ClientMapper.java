package com.ar.edu.um.montana.cars.service.mapper;

import com.ar.edu.um.montana.cars.domain.Client;
import com.ar.edu.um.montana.cars.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {}
