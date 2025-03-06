package com.ar.edu.um.taccetta.cars.service.impl;

import com.ar.edu.um.taccetta.cars.domain.PurchasedCar;
import com.ar.edu.um.taccetta.cars.repository.PurchasedCarRepository;
import com.ar.edu.um.taccetta.cars.service.PurchasedCarService;
import com.ar.edu.um.taccetta.cars.service.dto.PurchasedCarDTO;
import com.ar.edu.um.taccetta.cars.service.mapper.PurchasedCarMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ar.edu.um.taccetta.cars.domain.PurchasedCar}.
 */
@Service
@Transactional
public class PurchasedCarServiceImpl implements PurchasedCarService {

    private static final Logger LOG = LoggerFactory.getLogger(PurchasedCarServiceImpl.class);

    private final PurchasedCarRepository purchasedCarRepository;

    private final PurchasedCarMapper purchasedCarMapper;

    public PurchasedCarServiceImpl(PurchasedCarRepository purchasedCarRepository, PurchasedCarMapper purchasedCarMapper) {
        this.purchasedCarRepository = purchasedCarRepository;
        this.purchasedCarMapper = purchasedCarMapper;
    }

    @Override
    public PurchasedCarDTO save(PurchasedCarDTO purchasedCarDTO) {
        LOG.debug("Request to save PurchasedCar : {}", purchasedCarDTO);
        PurchasedCar purchasedCar = purchasedCarMapper.toEntity(purchasedCarDTO);
        purchasedCar = purchasedCarRepository.save(purchasedCar);
        return purchasedCarMapper.toDto(purchasedCar);
    }

    @Override
    public PurchasedCarDTO update(PurchasedCarDTO purchasedCarDTO) {
        LOG.debug("Request to update PurchasedCar : {}", purchasedCarDTO);
        PurchasedCar purchasedCar = purchasedCarMapper.toEntity(purchasedCarDTO);
        purchasedCar = purchasedCarRepository.save(purchasedCar);
        return purchasedCarMapper.toDto(purchasedCar);
    }

    @Override
    public Optional<PurchasedCarDTO> partialUpdate(PurchasedCarDTO purchasedCarDTO) {
        LOG.debug("Request to partially update PurchasedCar : {}", purchasedCarDTO);

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
    public Optional<PurchasedCarDTO> findOne(Long id) {
        LOG.debug("Request to get PurchasedCar : {}", id);
        return purchasedCarRepository.findById(id).map(purchasedCarMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PurchasedCar : {}", id);
        purchasedCarRepository.deleteById(id);
    }
}
