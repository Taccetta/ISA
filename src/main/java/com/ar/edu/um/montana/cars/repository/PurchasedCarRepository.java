package com.ar.edu.um.montana.cars.repository;

import com.ar.edu.um.montana.cars.domain.PurchasedCar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchasedCar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchasedCarRepository extends JpaRepository<PurchasedCar, Long>, JpaSpecificationExecutor<PurchasedCar> {}
