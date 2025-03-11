package com.ar.edu.um.taccetta.cars.service;

import com.ar.edu.um.taccetta.cars.service.dto.ManufacturerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the manufacturers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ManufacturerDTO> findAll(Pageable pageable);

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
