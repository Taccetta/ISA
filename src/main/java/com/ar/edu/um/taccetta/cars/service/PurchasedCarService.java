package com.ar.edu.um.taccetta.cars.service;

import com.ar.edu.um.taccetta.cars.service.dto.PurchasedCarDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ar.edu.um.taccetta.cars.domain.PurchasedCar}.
 */
public interface PurchasedCarService {
    /**
     * Save a purchasedCar.
     *
     * @param purchasedCarDTO the entity to save.
     * @return the persisted entity.
     */
    PurchasedCarDTO save(PurchasedCarDTO purchasedCarDTO);

    /**
     * Updates a purchasedCar.
     *
     * @param purchasedCarDTO the entity to update.
     * @return the persisted entity.
     */
    PurchasedCarDTO update(PurchasedCarDTO purchasedCarDTO);

    /**
     * Partially updates a purchasedCar.
     *
     * @param purchasedCarDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PurchasedCarDTO> partialUpdate(PurchasedCarDTO purchasedCarDTO);

    /**
     * Get all the purchasedCars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchasedCarDTO> findAll(Pageable pageable);

    /**
     * Get all the purchasedCars with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchasedCarDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" purchasedCar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PurchasedCarDTO> findOne(Long id);

    /**
     * Delete the "id" purchasedCar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
