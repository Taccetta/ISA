package com.ar.edu.um.montana.cars.service;

import com.ar.edu.um.montana.cars.service.dto.PurchasedCarDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.ar.edu.um.montana.cars.domain.PurchasedCar}.
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
