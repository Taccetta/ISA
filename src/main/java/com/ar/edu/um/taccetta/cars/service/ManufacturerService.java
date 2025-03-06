package com.ar.edu.um.taccetta.cars.service;

import com.ar.edu.um.taccetta.cars.service.dto.ManufacturerDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.ar.edu.um.taccetta.cars.domain.Manufacturer}.
 */
public interface ManufacturerService {
    /**
     * Save a manufacturer.
     *
     * @param manufacturerDTO the entity to save.
     * @return the persisted entity.
     */
    ManufacturerDTO save(ManufacturerDTO manufacturerDTO);

    /**
     * Updates a manufacturer.
     *
     * @param manufacturerDTO the entity to update.
     * @return the persisted entity.
     */
    ManufacturerDTO update(ManufacturerDTO manufacturerDTO);

    /**
     * Partially updates a manufacturer.
     *
     * @param manufacturerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ManufacturerDTO> partialUpdate(ManufacturerDTO manufacturerDTO);

    /**
     * Get all the ManufacturerDTO where Car is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ManufacturerDTO> findAllWhereCarIsNull();

    /**
     * Get the "id" manufacturer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ManufacturerDTO> findOne(Long id);

    /**
     * Delete the "id" manufacturer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
