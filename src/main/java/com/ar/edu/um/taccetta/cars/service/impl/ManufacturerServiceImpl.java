package com.ar.edu.um.taccetta.cars.service.impl;

import com.ar.edu.um.taccetta.cars.domain.Manufacturer;
import com.ar.edu.um.taccetta.cars.repository.ManufacturerRepository;
import com.ar.edu.um.taccetta.cars.service.ManufacturerService;
import com.ar.edu.um.taccetta.cars.service.dto.ManufacturerDTO;
import com.ar.edu.um.taccetta.cars.service.mapper.ManufacturerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Manufacturer}.
 */
@Service
@Transactional
public class ManufacturerServiceImpl implements ManufacturerService {

    private final Logger log = LoggerFactory.getLogger(ManufacturerServiceImpl.class);

    private final ManufacturerRepository manufacturerRepository;

    private final ManufacturerMapper manufacturerMapper;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, ManufacturerMapper manufacturerMapper) {
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerMapper = manufacturerMapper;
    }

    @Override
    public ManufacturerDTO save(ManufacturerDTO manufacturerDTO) {
        log.debug("Request to save Manufacturer : {}", manufacturerDTO);
        Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDTO);
        manufacturer = manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }

    @Override
    public ManufacturerDTO update(ManufacturerDTO manufacturerDTO) {
        log.debug("Request to update Manufacturer : {}", manufacturerDTO);
        Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDTO);
        manufacturer = manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }

    @Override
    public Optional<ManufacturerDTO> partialUpdate(ManufacturerDTO manufacturerDTO) {
        log.debug("Request to partially update Manufacturer : {}", manufacturerDTO);

        return manufacturerRepository
            .findById(manufacturerDTO.getId())
            .map(existingManufacturer -> {
                manufacturerMapper.partialUpdate(existingManufacturer, manufacturerDTO);

                return existingManufacturer;
            })
            .map(manufacturerRepository::save)
            .map(manufacturerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManufacturerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Manufacturers");
        return manufacturerRepository.findAll(pageable).map(manufacturerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManufacturerDTO> findOne(Long id) {
        log.debug("Request to get Manufacturer : {}", id);
        return manufacturerRepository.findById(id).map(manufacturerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Manufacturer : {}", id);
        manufacturerRepository.deleteById(id);
    }
}
