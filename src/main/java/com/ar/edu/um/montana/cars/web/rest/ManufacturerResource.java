package com.ar.edu.um.montana.cars.web.rest;

import com.ar.edu.um.montana.cars.repository.ManufacturerRepository;
import com.ar.edu.um.montana.cars.service.ManufacturerQueryService;
import com.ar.edu.um.montana.cars.service.ManufacturerService;
import com.ar.edu.um.montana.cars.service.criteria.ManufacturerCriteria;
import com.ar.edu.um.montana.cars.service.dto.ManufacturerDTO;
import com.ar.edu.um.montana.cars.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.ar.edu.um.montana.cars.domain.Manufacturer}.
 */
@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerResource {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerResource.class);

    private static final String ENTITY_NAME = "manufacturer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManufacturerService manufacturerService;

    private final ManufacturerRepository manufacturerRepository;

    private final ManufacturerQueryService manufacturerQueryService;

    public ManufacturerResource(
        ManufacturerService manufacturerService,
        ManufacturerRepository manufacturerRepository,
        ManufacturerQueryService manufacturerQueryService
    ) {
        this.manufacturerService = manufacturerService;
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerQueryService = manufacturerQueryService;
    }

    /**
     * {@code POST  /manufacturers} : Create a new manufacturer.
     *
     * @param manufacturerDTO the manufacturerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manufacturerDTO, or with status {@code 400 (Bad Request)} if the manufacturer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ManufacturerDTO> createManufacturer(@Valid @RequestBody ManufacturerDTO manufacturerDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Manufacturer : {}", manufacturerDTO);
        if (manufacturerDTO.getId() != null) {
            throw new BadRequestAlertException("A new manufacturer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        manufacturerDTO = manufacturerService.save(manufacturerDTO);
        return ResponseEntity.created(new URI("/api/manufacturers/" + manufacturerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, manufacturerDTO.getId().toString()))
            .body(manufacturerDTO);
    }

    /**
     * {@code PUT  /manufacturers/:id} : Updates an existing manufacturer.
     *
     * @param id the id of the manufacturerDTO to save.
     * @param manufacturerDTO the manufacturerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manufacturerDTO,
     * or with status {@code 400 (Bad Request)} if the manufacturerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manufacturerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerDTO> updateManufacturer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ManufacturerDTO manufacturerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Manufacturer : {}, {}", id, manufacturerDTO);
        if (manufacturerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manufacturerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manufacturerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        manufacturerDTO = manufacturerService.update(manufacturerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manufacturerDTO.getId().toString()))
            .body(manufacturerDTO);
    }

    /**
     * {@code PATCH  /manufacturers/:id} : Partial updates given fields of an existing manufacturer, field will ignore if it is null
     *
     * @param id the id of the manufacturerDTO to save.
     * @param manufacturerDTO the manufacturerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manufacturerDTO,
     * or with status {@code 400 (Bad Request)} if the manufacturerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the manufacturerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the manufacturerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ManufacturerDTO> partialUpdateManufacturer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ManufacturerDTO manufacturerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Manufacturer partially : {}, {}", id, manufacturerDTO);
        if (manufacturerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manufacturerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manufacturerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ManufacturerDTO> result = manufacturerService.partialUpdate(manufacturerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manufacturerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /manufacturers} : get all the manufacturers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manufacturers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ManufacturerDTO>> getAllManufacturers(
        ManufacturerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Manufacturers by criteria: {}", criteria);

        Page<ManufacturerDTO> page = manufacturerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /manufacturers/count} : count all the manufacturers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countManufacturers(ManufacturerCriteria criteria) {
        LOG.debug("REST request to count Manufacturers by criteria: {}", criteria);
        return ResponseEntity.ok().body(manufacturerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /manufacturers/:id} : get the "id" manufacturer.
     *
     * @param id the id of the manufacturerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manufacturerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerDTO> getManufacturer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Manufacturer : {}", id);
        Optional<ManufacturerDTO> manufacturerDTO = manufacturerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manufacturerDTO);
    }

    /**
     * {@code DELETE  /manufacturers/:id} : delete the "id" manufacturer.
     *
     * @param id the id of the manufacturerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Manufacturer : {}", id);
        manufacturerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
