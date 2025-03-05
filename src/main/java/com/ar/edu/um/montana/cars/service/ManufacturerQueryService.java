package com.ar.edu.um.montana.cars.service;

import com.ar.edu.um.montana.cars.domain.*; // for static metamodels
import com.ar.edu.um.montana.cars.domain.Manufacturer;
import com.ar.edu.um.montana.cars.repository.ManufacturerRepository;
import com.ar.edu.um.montana.cars.service.criteria.ManufacturerCriteria;
import com.ar.edu.um.montana.cars.service.dto.ManufacturerDTO;
import com.ar.edu.um.montana.cars.service.mapper.ManufacturerMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Manufacturer} entities in the database.
 * The main input is a {@link ManufacturerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ManufacturerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ManufacturerQueryService extends QueryService<Manufacturer> {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerQueryService.class);

    private final ManufacturerRepository manufacturerRepository;

    private final ManufacturerMapper manufacturerMapper;

    public ManufacturerQueryService(ManufacturerRepository manufacturerRepository, ManufacturerMapper manufacturerMapper) {
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerMapper = manufacturerMapper;
    }

    /**
     * Return a {@link Page} of {@link ManufacturerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ManufacturerDTO> findByCriteria(ManufacturerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Manufacturer> specification = createSpecification(criteria);
        return manufacturerRepository.findAll(specification, page).map(manufacturerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ManufacturerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Manufacturer> specification = createSpecification(criteria);
        return manufacturerRepository.count(specification);
    }

    /**
     * Function to convert {@link ManufacturerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Manufacturer> createSpecification(ManufacturerCriteria criteria) {
        Specification<Manufacturer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Manufacturer_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Manufacturer_.name));
            }
            if (criteria.getCarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCarId(), root -> root.join(Manufacturer_.car, JoinType.LEFT).get(Car_.id))
                );
            }
        }
        return specification;
    }
}
