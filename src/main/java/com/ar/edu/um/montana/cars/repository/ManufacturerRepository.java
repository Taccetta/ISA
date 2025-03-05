package com.ar.edu.um.montana.cars.repository;

import com.ar.edu.um.montana.cars.domain.Manufacturer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Manufacturer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long>, JpaSpecificationExecutor<Manufacturer> {}
