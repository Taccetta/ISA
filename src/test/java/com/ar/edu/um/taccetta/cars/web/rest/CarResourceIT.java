package com.ar.edu.um.taccetta.cars.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ar.edu.um.taccetta.cars.IntegrationTest;
import com.ar.edu.um.taccetta.cars.domain.Car;
import com.ar.edu.um.taccetta.cars.domain.Manufacturer;
import com.ar.edu.um.taccetta.cars.repository.CarRepository;
import com.ar.edu.um.taccetta.cars.service.CarService;
import com.ar.edu.um.taccetta.cars.service.criteria.CarCriteria;
import com.ar.edu.um.taccetta.cars.service.dto.CarDTO;
import com.ar.edu.um.taccetta.cars.service.mapper.CarMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CarResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarResourceIT {

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final Integer DEFAULT_AVAILABLE = 1;
    private static final Integer UPDATED_AVAILABLE = 2;
    private static final Integer SMALLER_AVAILABLE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarRepository carRepository;

    @Mock
    private CarRepository carRepositoryMock;

    @Autowired
    private CarMapper carMapper;

    @Mock
    private CarService carServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarMockMvc;

    private Car car;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity(EntityManager em) {
        Car car = new Car().model(DEFAULT_MODEL).year(DEFAULT_YEAR).available(DEFAULT_AVAILABLE);
        return car;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity(EntityManager em) {
        Car car = new Car().model(UPDATED_MODEL).year(UPDATED_YEAR).available(UPDATED_AVAILABLE);
        return car;
    }

    @BeforeEach
    public void initTest() {
        car = createEntity(em);
    }

    @Test
    @Transactional
    void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();
        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testCar.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testCar.getAvailable()).isEqualTo(DEFAULT_AVAILABLE);
    }

    @Test
    @Transactional
    void createCarWithExistingId() throws Exception {
        // Create the Car with an existing ID
        car.setId(1L);
        CarDTO carDTO = carMapper.toDto(car);

        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().size();
        // set the field null
        car.setModel(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isBadRequest());

        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().size();
        // set the field null
        car.setYear(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isBadRequest());

        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().size();
        // set the field null
        car.setAvailable(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isBadRequest());

        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarsWithEagerRelationshipsIsEnabled() throws Exception {
        when(carServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(carRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc
            .perform(get(ENTITY_API_URL_ID, car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE));
    }

    @Test
    @Transactional
    void getCarsByIdFiltering() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        Long id = car.getId();

        defaultCarShouldBeFound("id.equals=" + id);
        defaultCarShouldNotBeFound("id.notEquals=" + id);

        defaultCarShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.greaterThan=" + id);

        defaultCarShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where model equals to DEFAULT_MODEL
        defaultCarShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the carList where model equals to UPDATED_MODEL
        defaultCarShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultCarShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the carList where model equals to UPDATED_MODEL
        defaultCarShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where model is not null
        defaultCarShouldBeFound("model.specified=true");

        // Get all the carList where model is null
        defaultCarShouldNotBeFound("model.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByModelContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where model contains DEFAULT_MODEL
        defaultCarShouldBeFound("model.contains=" + DEFAULT_MODEL);

        // Get all the carList where model contains UPDATED_MODEL
        defaultCarShouldNotBeFound("model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where model does not contain DEFAULT_MODEL
        defaultCarShouldNotBeFound("model.doesNotContain=" + DEFAULT_MODEL);

        // Get all the carList where model does not contain UPDATED_MODEL
        defaultCarShouldBeFound("model.doesNotContain=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where year equals to DEFAULT_YEAR
        defaultCarShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the carList where year equals to UPDATED_YEAR
        defaultCarShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByYearIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultCarShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the carList where year equals to UPDATED_YEAR
        defaultCarShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where year is not null
        defaultCarShouldBeFound("year.specified=true");

        // Get all the carList where year is null
        defaultCarShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByYearContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where year contains DEFAULT_YEAR
        defaultCarShouldBeFound("year.contains=" + DEFAULT_YEAR);

        // Get all the carList where year contains UPDATED_YEAR
        defaultCarShouldNotBeFound("year.contains=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByYearNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where year does not contain DEFAULT_YEAR
        defaultCarShouldNotBeFound("year.doesNotContain=" + DEFAULT_YEAR);

        // Get all the carList where year does not contain UPDATED_YEAR
        defaultCarShouldBeFound("year.doesNotContain=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where available equals to DEFAULT_AVAILABLE
        defaultCarShouldBeFound("available.equals=" + DEFAULT_AVAILABLE);

        // Get all the carList where available equals to UPDATED_AVAILABLE
        defaultCarShouldNotBeFound("available.equals=" + UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where available in DEFAULT_AVAILABLE or UPDATED_AVAILABLE
        defaultCarShouldBeFound("available.in=" + DEFAULT_AVAILABLE + "," + UPDATED_AVAILABLE);

        // Get all the carList where available equals to UPDATED_AVAILABLE
        defaultCarShouldNotBeFound("available.in=" + UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where available is not null
        defaultCarShouldBeFound("available.specified=true");

        // Get all the carList where available is null
        defaultCarShouldNotBeFound("available.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where available is greater than or equal to DEFAULT_AVAILABLE
        defaultCarShouldBeFound("available.greaterThanOrEqual=" + DEFAULT_AVAILABLE);

        // Get all the carList where available is greater than or equal to UPDATED_AVAILABLE
        defaultCarShouldNotBeFound("available.greaterThanOrEqual=" + UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where available is less than or equal to DEFAULT_AVAILABLE
        defaultCarShouldBeFound("available.lessThanOrEqual=" + DEFAULT_AVAILABLE);

        // Get all the carList where available is less than or equal to SMALLER_AVAILABLE
        defaultCarShouldNotBeFound("available.lessThanOrEqual=" + SMALLER_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsLessThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where available is less than DEFAULT_AVAILABLE
        defaultCarShouldNotBeFound("available.lessThan=" + DEFAULT_AVAILABLE);

        // Get all the carList where available is less than UPDATED_AVAILABLE
        defaultCarShouldBeFound("available.lessThan=" + UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where available is greater than DEFAULT_AVAILABLE
        defaultCarShouldNotBeFound("available.greaterThan=" + DEFAULT_AVAILABLE);

        // Get all the carList where available is greater than SMALLER_AVAILABLE
        defaultCarShouldBeFound("available.greaterThan=" + SMALLER_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByManufacturerIsEqualToSomething() throws Exception {
        Manufacturer manufacturer;
        if (TestUtil.findAll(em, Manufacturer.class).isEmpty()) {
            carRepository.saveAndFlush(car);
            manufacturer = ManufacturerResourceIT.createEntity(em);
        } else {
            manufacturer = TestUtil.findAll(em, Manufacturer.class).get(0);
        }
        em.persist(manufacturer);
        em.flush();
        car.setManufacturer(manufacturer);
        carRepository.saveAndFlush(car);
        Long manufacturerId = manufacturer.getId();

        // Get all the carList where manufacturer equals to manufacturerId
        defaultCarShouldBeFound("manufacturerId.equals=" + manufacturerId);

        // Get all the carList where manufacturer equals to (manufacturerId + 1)
        defaultCarShouldNotBeFound("manufacturerId.equals=" + (manufacturerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarShouldBeFound(String filter) throws Exception {
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE)));

        // Check, that the count call also returns 1
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarShouldNotBeFound(String filter) throws Exception {
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).get();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar.model(UPDATED_MODEL).year(UPDATED_YEAR).available(UPDATED_AVAILABLE);
        CarDTO carDTO = carMapper.toDto(updatedCar);

        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testCar.getAvailable()).isEqualTo(UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void putNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar.model(UPDATED_MODEL).year(UPDATED_YEAR).available(UPDATED_AVAILABLE);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testCar.getAvailable()).isEqualTo(UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void fullUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar.model(UPDATED_MODEL).year(UPDATED_YEAR).available(UPDATED_AVAILABLE);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testCar.getAvailable()).isEqualTo(UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void patchNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeDelete = carRepository.findAll().size();

        // Delete the car
        restCarMockMvc.perform(delete(ENTITY_API_URL_ID, car.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
