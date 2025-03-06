package com.ar.edu.um.taccetta.cars.service;

import com.ar.edu.um.taccetta.cars.domain.*; // for static metamodels
import com.ar.edu.um.taccetta.cars.domain.Car;
import com.ar.edu.um.taccetta.cars.repository.CarRepository;
import com.ar.edu.um.taccetta.cars.service.criteria.CarCriteria;
import com.ar.edu.um.taccetta.cars.service.dto.CarDTO;
import com.ar.edu.um.taccetta.cars.service.mapper.CarMapper;
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
 * Service for executing complex queries for {@link Car} entities in the database.
 * The main input is a {@link CarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CarDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CarQueryService extends QueryService<Car> {

    private static final Logger LOG = LoggerFactory.getLogger(CarQueryService.class);

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    public CarQueryService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    /**
     * Return a {@link Page} of {@link CarDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CarDTO> findByCriteria(CarCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.findAll(specification, page).map(carMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.count(specification);
    }

    /**
     * Function to convert {@link CarCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Car> createSpecification(CarCriteria criteria) {
        Specification<Car> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Car_.id));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModel(), Car_.model));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildStringSpecification(criteria.getYear(), Car_.year));
            }
            if (criteria.getAvailable() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvailable(), Car_.available));
            }
            if (criteria.getManufacturerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getManufacturerId(), root ->
                        root.join(Car_.manufacturer, JoinType.LEFT).get(Manufacturer_.id)
                    )
                );
            }
            if (criteria.getPurchasedCarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPurchasedCarId(), root ->
                        root.join(Car_.purchasedCar, JoinType.LEFT).get(PurchasedCar_.id)
                    )
                );
            }
        }
        return specification;
    }
}
