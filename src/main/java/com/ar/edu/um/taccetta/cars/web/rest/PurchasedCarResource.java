package com.ar.edu.um.taccetta.cars.web.rest;

import com.ar.edu.um.taccetta.cars.repository.PurchasedCarRepository;
import com.ar.edu.um.taccetta.cars.service.PurchasedCarQueryService;
import com.ar.edu.um.taccetta.cars.service.PurchasedCarService;
import com.ar.edu.um.taccetta.cars.service.criteria.PurchasedCarCriteria;
import com.ar.edu.um.taccetta.cars.service.dto.PurchasedCarDTO;
import com.ar.edu.um.taccetta.cars.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ar.edu.um.taccetta.cars.domain.PurchasedCar}.
 */
@RestController
@RequestMapping("/api/purchased-cars")
public class PurchasedCarResource {

    private static final Logger LOG = LoggerFactory.getLogger(PurchasedCarResource.class);

    private static final String ENTITY_NAME = "purchasedCar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchasedCarService purchasedCarService;

    private final PurchasedCarRepository purchasedCarRepository;

    private final PurchasedCarQueryService purchasedCarQueryService;

    public PurchasedCarResource(
        PurchasedCarService purchasedCarService,
        PurchasedCarRepository purchasedCarRepository,
        PurchasedCarQueryService purchasedCarQueryService
    ) {
        this.purchasedCarService = purchasedCarService;
        this.purchasedCarRepository = purchasedCarRepository;
        this.purchasedCarQueryService = purchasedCarQueryService;
    }

    /**
     * {@code POST  /purchased-cars} : Create a new purchasedCar.
     *
     * @param purchasedCarDTO the purchasedCarDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchasedCarDTO, or with status {@code 400 (Bad Request)} if the purchasedCar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PurchasedCarDTO> createPurchasedCar(@RequestBody PurchasedCarDTO purchasedCarDTO) throws URISyntaxException {
        LOG.debug("REST request to save PurchasedCar : {}", purchasedCarDTO);
        if (purchasedCarDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchasedCar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        purchasedCarDTO = purchasedCarService.save(purchasedCarDTO);
        return ResponseEntity.created(new URI("/api/purchased-cars/" + purchasedCarDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, purchasedCarDTO.getId().toString()))
            .body(purchasedCarDTO);
    }

    /**
     * {@code PUT  /purchased-cars/:id} : Updates an existing purchasedCar.
     *
     * @param id the id of the purchasedCarDTO to save.
     * @param purchasedCarDTO the purchasedCarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedCarDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedCarDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchasedCarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PurchasedCarDTO> updatePurchasedCar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchasedCarDTO purchasedCarDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PurchasedCar : {}, {}", id, purchasedCarDTO);
        if (purchasedCarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedCarDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchasedCarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        purchasedCarDTO = purchasedCarService.update(purchasedCarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchasedCarDTO.getId().toString()))
            .body(purchasedCarDTO);
    }

    /**
     * {@code PATCH  /purchased-cars/:id} : Partial updates given fields of an existing purchasedCar, field will ignore if it is null
     *
     * @param id the id of the purchasedCarDTO to save.
     * @param purchasedCarDTO the purchasedCarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedCarDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedCarDTO is not valid,
     * or with status {@code 404 (Not Found)} if the purchasedCarDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchasedCarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchasedCarDTO> partialUpdatePurchasedCar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchasedCarDTO purchasedCarDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PurchasedCar partially : {}, {}", id, purchasedCarDTO);
        if (purchasedCarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedCarDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchasedCarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchasedCarDTO> result = purchasedCarService.partialUpdate(purchasedCarDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchasedCarDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /purchased-cars} : get all the purchasedCars.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchasedCars in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PurchasedCarDTO>> getAllPurchasedCars(
        PurchasedCarCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PurchasedCars by criteria: {}", criteria);

        Page<PurchasedCarDTO> page = purchasedCarQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchased-cars/count} : count all the purchasedCars.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPurchasedCars(PurchasedCarCriteria criteria) {
        LOG.debug("REST request to count PurchasedCars by criteria: {}", criteria);
        return ResponseEntity.ok().body(purchasedCarQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /purchased-cars/:id} : get the "id" purchasedCar.
     *
     * @param id the id of the purchasedCarDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchasedCarDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchasedCarDTO> getPurchasedCar(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PurchasedCar : {}", id);
        Optional<PurchasedCarDTO> purchasedCarDTO = purchasedCarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchasedCarDTO);
    }

    /**
     * {@code DELETE  /purchased-cars/:id} : delete the "id" purchasedCar.
     *
     * @param id the id of the purchasedCarDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchasedCar(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PurchasedCar : {}", id);
        purchasedCarService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
