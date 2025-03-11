package com.ar.edu.um.taccetta.cars.service.impl;

import com.ar.edu.um.taccetta.cars.domain.PurchasedCar;
import com.ar.edu.um.taccetta.cars.repository.PurchasedCarRepository;
import com.ar.edu.um.taccetta.cars.service.PurchasedCarService;
import com.ar.edu.um.taccetta.cars.service.dto.PurchasedCarDTO;
import com.ar.edu.um.taccetta.cars.service.mapper.PurchasedCarMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PurchasedCar}.
 */
@Service
@Transactional
public class PurchasedCarServiceImpl implements PurchasedCarService {

    private final Logger log = LoggerFactory.getLogger(PurchasedCarServiceImpl.class);

    private final PurchasedCarRepository purchasedCarRepository;

    private final PurchasedCarMapper purchasedCarMapper;

    public PurchasedCarServiceImpl(PurchasedCarRepository purchasedCarRepository, PurchasedCarMapper purchasedCarMapper) {
        this.purchasedCarRepository = purchasedCarRepository;
        this.purchasedCarMapper = purchasedCarMapper;
    }

    @Override
    public PurchasedCarDTO save(PurchasedCarDTO purchasedCarDTO) {
        log.debug("Request to save PurchasedCar : {}", purchasedCarDTO);
        PurchasedCar purchasedCar = purchasedCarMapper.toEntity(purchasedCarDTO);
        purchasedCar = purchasedCarRepository.save(purchasedCar);
        return purchasedCarMapper.toDto(purchasedCar);
    }

    @Override
    public PurchasedCarDTO update(PurchasedCarDTO purchasedCarDTO) {
        log.debug("Request to update PurchasedCar : {}", purchasedCarDTO);
        PurchasedCar purchasedCar = purchasedCarMapper.toEntity(purchasedCarDTO);
        purchasedCar = purchasedCarRepository.save(purchasedCar);
        return purchasedCarMapper.toDto(purchasedCar);
    }

    @Override
    public Optional<PurchasedCarDTO> partialUpdate(PurchasedCarDTO purchasedCarDTO) {
        log.debug("Request to partially update PurchasedCar : {}", purchasedCarDTO);

        return purchasedCarRepository
            .findById(purchasedCarDTO.getId())
            .map(existingPurchasedCar -> {
                purchasedCarMapper.partialUpdate(existingPurchasedCar, purchasedCarDTO);

                return existingPurchasedCar;
            })
            .map(purchasedCarRepository::save)
            .map(purchasedCarMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchasedCarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchasedCars");
        return purchasedCarRepository.findAll(pageable).map(purchasedCarMapper::toDto);
    }

    public Page<PurchasedCarDTO> findAllWithEagerRelationships(Pageable pageable) {
        return purchasedCarRepository.findAllWithEagerRelationships(pageable).map(purchasedCarMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchasedCarDTO> findOne(Long id) {
        log.debug("Request to get PurchasedCar : {}", id);
        return purchasedCarRepository.findOneWithEagerRelationships(id).map(purchasedCarMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PurchasedCar : {}", id);
        purchasedCarRepository.deleteById(id);
    }
}
