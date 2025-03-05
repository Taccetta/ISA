package com.ar.edu.um.montana.cars.web.rest;

import com.ar.edu.um.montana.cars.repository.CarRepository;
import com.ar.edu.um.montana.cars.service.CarQueryService;
import com.ar.edu.um.montana.cars.service.CarService;
import com.ar.edu.um.montana.cars.service.criteria.CarCriteria;
import com.ar.edu.um.montana.cars.service.dto.CarDTO;
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
 * REST controller for managing {@link com.ar.edu.um.montana.cars.domain.Car}.
 */
@RestController
@RequestMapping("/api/cars")
public class CarResource {

    private static final Logger LOG = LoggerFactory.getLogger(CarResource.class);

    private static final String ENTITY_NAME = "car";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarService carService;

    private final CarRepository carRepository;

    private final CarQueryService carQueryService;

    public CarResource(CarService carService, CarRepository carRepository, CarQueryService carQueryService) {
        this.carService = carService;
        this.carRepository = carRepository;
        this.carQueryService = carQueryService;
    }

    /**
     * {@code POST  /cars} : Create a new car.
     *
     * @param carDTO the carDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carDTO, or with status {@code 400 (Bad Request)} if the car has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CarDTO> createCar(@Valid @RequestBody CarDTO carDTO) throws URISyntaxException {
        LOG.debug("REST request to save Car : {}", carDTO);
        if (carDTO.getId() != null) {
            throw new BadRequestAlertException("A new car cannot already have an ID", ENTITY_NAME, "idexists");
        }
        carDTO = carService.save(carDTO);
        return ResponseEntity.created(new URI("/api/cars/" + carDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, carDTO.getId().toString()))
            .body(carDTO);
    }

    /**
     * {@code PUT  /cars/:id} : Updates an existing car.
     *
     * @param id the id of the carDTO to save.
     * @param carDTO the carDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carDTO,
     * or with status {@code 400 (Bad Request)} if the carDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarDTO> updateCar(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody CarDTO carDTO)
        throws URISyntaxException {
        LOG.debug("REST request to update Car : {}, {}", id, carDTO);
        if (carDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        carDTO = carService.update(carDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carDTO.getId().toString()))
            .body(carDTO);
    }

    /**
     * {@code PATCH  /cars/:id} : Partial updates given fields of an existing car, field will ignore if it is null
     *
     * @param id the id of the carDTO to save.
     * @param carDTO the carDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carDTO,
     * or with status {@code 400 (Bad Request)} if the carDTO is not valid,
     * or with status {@code 404 (Not Found)} if the carDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the carDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarDTO> partialUpdateCar(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarDTO carDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Car partially : {}, {}", id, carDTO);
        if (carDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarDTO> result = carService.partialUpdate(carDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cars} : get all the cars.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cars in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CarDTO>> getAllCars(
        CarCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Cars by criteria: {}", criteria);

        Page<CarDTO> page = carQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cars/count} : count all the cars.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCars(CarCriteria criteria) {
        LOG.debug("REST request to count Cars by criteria: {}", criteria);
        return ResponseEntity.ok().body(carQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cars/:id} : get the "id" car.
     *
     * @param id the id of the carDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCar(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Car : {}", id);
        Optional<CarDTO> carDTO = carService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carDTO);
    }

    /**
     * {@code DELETE  /cars/:id} : delete the "id" car.
     *
     * @param id the id of the carDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Car : {}", id);
        carService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
