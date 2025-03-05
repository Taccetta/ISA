package com.ar.edu.um.montana.cars.service.impl;

import com.ar.edu.um.montana.cars.domain.Manufacturer;
import com.ar.edu.um.montana.cars.repository.ManufacturerRepository;
import com.ar.edu.um.montana.cars.service.ManufacturerService;
import com.ar.edu.um.montana.cars.service.dto.ManufacturerDTO;
import com.ar.edu.um.montana.cars.service.mapper.ManufacturerMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ar.edu.um.montana.cars.domain.Manufacturer}.
 */
@Service
@Transactional
public class ManufacturerServiceImpl implements ManufacturerService {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerServiceImpl.class);

    private final ManufacturerRepository manufacturerRepository;

    private final ManufacturerMapper manufacturerMapper;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, ManufacturerMapper manufacturerMapper) {
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerMapper = manufacturerMapper;
    }

    @Override
    public ManufacturerDTO save(ManufacturerDTO manufacturerDTO) {
        LOG.debug("Request to save Manufacturer : {}", manufacturerDTO);
        Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDTO);
        manufacturer = manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }

    @Override
    public ManufacturerDTO update(ManufacturerDTO manufacturerDTO) {
        LOG.debug("Request to update Manufacturer : {}", manufacturerDTO);
        Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDTO);
        manufacturer = manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toDto(manufacturer);
    }

    @Override
    public Optional<ManufacturerDTO> partialUpdate(ManufacturerDTO manufacturerDTO) {
        LOG.debug("Request to partially update Manufacturer : {}", manufacturerDTO);

        return manufacturerRepository
            .findById(manufacturerDTO.getId())
            .map(existingManufacturer -> {
                manufacturerMapper.partialUpdate(existingManufacturer, manufacturerDTO);

                return existingManufacturer;
            })
            .map(manufacturerRepository::save)
            .map(manufacturerMapper::toDto);
    }

    /**
     *  Get all the manufacturers where Car is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ManufacturerDTO> findAllWhereCarIsNull() {
        LOG.debug("Request to get all manufacturers where Car is null");
        return StreamSupport.stream(manufacturerRepository.findAll().spliterator(), false)
            .filter(manufacturer -> manufacturer.getCar() == null)
            .map(manufacturerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManufacturerDTO> findOne(Long id) {
        LOG.debug("Request to get Manufacturer : {}", id);
        return manufacturerRepository.findById(id).map(manufacturerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Manufacturer : {}", id);
        manufacturerRepository.deleteById(id);
    }
}
