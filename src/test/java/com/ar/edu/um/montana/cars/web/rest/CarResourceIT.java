package com.ar.edu.um.montana.cars.web.rest;

import static com.ar.edu.um.montana.cars.domain.CarAsserts.*;
import static com.ar.edu.um.montana.cars.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ar.edu.um.montana.cars.IntegrationTest;
import com.ar.edu.um.montana.cars.domain.Car;
import com.ar.edu.um.montana.cars.domain.Manufacturer;
import com.ar.edu.um.montana.cars.repository.CarRepository;
import com.ar.edu.um.montana.cars.service.dto.CarDTO;
import com.ar.edu.um.montana.cars.service.mapper.CarMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CarResource} REST controller.
 */
@IntegrationTest
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
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarMockMvc;

    private Car car;

    private Car insertedCar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity() {
        return new Car().model(DEFAULT_MODEL).year(DEFAULT_YEAR).available(DEFAULT_AVAILABLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity() {
        return new Car().model(UPDATED_MODEL).year(UPDATED_YEAR).available(UPDATED_AVAILABLE);
    }

    @BeforeEach
    public void initTest() {
        car = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCar != null) {
            carRepository.delete(insertedCar);
            insertedCar = null;
        }
    }

    @Test
    @Transactional
    void createCar() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);
        var returnedCarDTO = om.readValue(
            restCarMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CarDTO.class
        );

        // Validate the Car in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCar = carMapper.toEntity(returnedCarDTO);
        assertCarUpdatableFieldsEquals(returnedCar, getPersistedCar(returnedCar));

        insertedCar = returnedCar;
    }

    @Test
    @Transactional
    void createCarWithExistingId() throws Exception {
        // Create the Car with an existing ID
        car.setId(1L);
        CarDTO carDTO = carMapper.toDto(car);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkModelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        car.setModel(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        car.setYear(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        car.setAvailable(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCars() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

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

    @Test
    @Transactional
    void getCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

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
        insertedCar = carRepository.saveAndFlush(car);

        Long id = car.getId();

        defaultCarFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCarFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCarFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where model equals to
        defaultCarFiltering("model.equals=" + DEFAULT_MODEL, "model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where model in
        defaultCarFiltering("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL, "model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where model is not null
        defaultCarFiltering("model.specified=true", "model.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByModelContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where model contains
        defaultCarFiltering("model.contains=" + DEFAULT_MODEL, "model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where model does not contain
        defaultCarFiltering("model.doesNotContain=" + UPDATED_MODEL, "model.doesNotContain=" + DEFAULT_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where year equals to
        defaultCarFiltering("year.equals=" + DEFAULT_YEAR, "year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where year in
        defaultCarFiltering("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR, "year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where year is not null
        defaultCarFiltering("year.specified=true", "year.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByYearContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where year contains
        defaultCarFiltering("year.contains=" + DEFAULT_YEAR, "year.contains=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByYearNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where year does not contain
        defaultCarFiltering("year.doesNotContain=" + UPDATED_YEAR, "year.doesNotContain=" + DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where available equals to
        defaultCarFiltering("available.equals=" + DEFAULT_AVAILABLE, "available.equals=" + UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where available in
        defaultCarFiltering("available.in=" + DEFAULT_AVAILABLE + "," + UPDATED_AVAILABLE, "available.in=" + UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where available is not null
        defaultCarFiltering("available.specified=true", "available.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where available is greater than or equal to
        defaultCarFiltering("available.greaterThanOrEqual=" + DEFAULT_AVAILABLE, "available.greaterThanOrEqual=" + UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where available is less than or equal to
        defaultCarFiltering("available.lessThanOrEqual=" + DEFAULT_AVAILABLE, "available.lessThanOrEqual=" + SMALLER_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where available is less than
        defaultCarFiltering("available.lessThan=" + UPDATED_AVAILABLE, "available.lessThan=" + DEFAULT_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByAvailableIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where available is greater than
        defaultCarFiltering("available.greaterThan=" + SMALLER_AVAILABLE, "available.greaterThan=" + DEFAULT_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllCarsByManufacturerIsEqualToSomething() throws Exception {
        Manufacturer manufacturer;
        if (TestUtil.findAll(em, Manufacturer.class).isEmpty()) {
            carRepository.saveAndFlush(car);
            manufacturer = ManufacturerResourceIT.createEntity();
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

    private void defaultCarFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCarShouldBeFound(shouldBeFound);
        defaultCarShouldNotBeFound(shouldNotBeFound);
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
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar.model(UPDATED_MODEL).year(UPDATED_YEAR).available(UPDATED_AVAILABLE);
        CarDTO carDTO = carMapper.toDto(updatedCar);

        restCarMockMvc
            .perform(put(ENTITY_API_URL_ID, carDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isOk());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarToMatchAllProperties(updatedCar);
    }

    @Test
    @Transactional
    void putNonExistingCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL_ID, carDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarWithPatch() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar.model(UPDATED_MODEL).year(UPDATED_YEAR).available(UPDATED_AVAILABLE);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCar, car), getPersistedCar(car));
    }

    @Test
    @Transactional
    void fullUpdateCarWithPatch() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar.model(UPDATED_MODEL).year(UPDATED_YEAR).available(UPDATED_AVAILABLE);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarUpdatableFieldsEquals(partialUpdatedCar, getPersistedCar(partialUpdatedCar));
    }

    @Test
    @Transactional
    void patchNonExistingCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the car
        restCarMockMvc.perform(delete(ENTITY_API_URL_ID, car.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Car getPersistedCar(Car car) {
        return carRepository.findById(car.getId()).orElseThrow();
    }

    protected void assertPersistedCarToMatchAllProperties(Car expectedCar) {
        assertCarAllPropertiesEquals(expectedCar, getPersistedCar(expectedCar));
    }

    protected void assertPersistedCarToMatchUpdatableProperties(Car expectedCar) {
        assertCarAllUpdatablePropertiesEquals(expectedCar, getPersistedCar(expectedCar));
    }
}
