package com.ar.edu.um.montana.cars.service;

import com.ar.edu.um.montana.cars.service.dto.CarDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.ar.edu.um.montana.cars.domain.Car}.
 */
public interface CarService {
    /**
     * Save a car.
     *
     * @param carDTO the entity to save.
     * @return the persisted entity.
     */
    CarDTO save(CarDTO carDTO);

    /**
     * Updates a car.
     *
     * @param carDTO the entity to update.
     * @return the persisted entity.
     */
    CarDTO update(CarDTO carDTO);

    /**
     * Partially updates a car.
     *
     * @param carDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CarDTO> partialUpdate(CarDTO carDTO);

    /**
     * Get all the CarDTO where PurchasedCar is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CarDTO> findAllWherePurchasedCarIsNull();

    /**
     * Get the "id" car.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarDTO> findOne(Long id);

    /**
     * Delete the "id" car.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
