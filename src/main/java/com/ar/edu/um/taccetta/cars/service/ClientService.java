package com.ar.edu.um.taccetta.cars.service;

import com.ar.edu.um.taccetta.cars.service.dto.ClientDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.ar.edu.um.taccetta.cars.domain.Client}.
 */
public interface ClientService {
    /**
     * Save a client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     */
    ClientDTO save(ClientDTO clientDTO);

    /**
     * Updates a client.
     *
     * @param clientDTO the entity to update.
     * @return the persisted entity.
     */
    ClientDTO update(ClientDTO clientDTO);

    /**
     * Partially updates a client.
     *
     * @param clientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClientDTO> partialUpdate(ClientDTO clientDTO);

    /**
     * Get all the ClientDTO where PurchasedCar is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ClientDTO> findAllWherePurchasedCarIsNull();

    /**
     * Get the "id" client.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClientDTO> findOne(Long id);

    /**
     * Delete the "id" client.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
