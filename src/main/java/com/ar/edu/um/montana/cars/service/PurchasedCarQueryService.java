package com.ar.edu.um.montana.cars.service;

import com.ar.edu.um.montana.cars.domain.*; // for static metamodels
import com.ar.edu.um.montana.cars.domain.PurchasedCar;
import com.ar.edu.um.montana.cars.repository.PurchasedCarRepository;
import com.ar.edu.um.montana.cars.service.criteria.PurchasedCarCriteria;
import com.ar.edu.um.montana.cars.service.dto.PurchasedCarDTO;
import com.ar.edu.um.montana.cars.service.mapper.PurchasedCarMapper;
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
 * Service for executing complex queries for {@link PurchasedCar} entities in the database.
 * The main input is a {@link PurchasedCarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PurchasedCarDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PurchasedCarQueryService extends QueryService<PurchasedCar> {

    private static final Logger LOG = LoggerFactory.getLogger(PurchasedCarQueryService.class);

    private final PurchasedCarRepository purchasedCarRepository;

    private final PurchasedCarMapper purchasedCarMapper;

    public PurchasedCarQueryService(PurchasedCarRepository purchasedCarRepository, PurchasedCarMapper purchasedCarMapper) {
        this.purchasedCarRepository = purchasedCarRepository;
        this.purchasedCarMapper = purchasedCarMapper;
    }

    /**
     * Return a {@link Page} of {@link PurchasedCarDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchasedCarDTO> findByCriteria(PurchasedCarCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PurchasedCar> specification = createSpecification(criteria);
        return purchasedCarRepository.findAll(specification, page).map(purchasedCarMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PurchasedCarCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PurchasedCar> specification = createSpecification(criteria);
        return purchasedCarRepository.count(specification);
    }

    /**
     * Function to convert {@link PurchasedCarCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PurchasedCar> createSpecification(PurchasedCarCriteria criteria) {
        Specification<PurchasedCar> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PurchasedCar_.id));
            }
            if (criteria.getPurchaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPurchaseDate(), PurchasedCar_.purchaseDate));
            }
            if (criteria.getCarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCarId(), root -> root.join(PurchasedCar_.car, JoinType.LEFT).get(Car_.id))
                );
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClientId(), root -> root.join(PurchasedCar_.client, JoinType.LEFT).get(Client_.id))
                );
            }
        }
        return specification;
    }
}
