package com.ar.edu.um.montana.cars.web.rest;

import static com.ar.edu.um.montana.cars.domain.PurchasedCarAsserts.*;
import static com.ar.edu.um.montana.cars.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ar.edu.um.montana.cars.IntegrationTest;
import com.ar.edu.um.montana.cars.domain.Car;
import com.ar.edu.um.montana.cars.domain.Client;
import com.ar.edu.um.montana.cars.domain.PurchasedCar;
import com.ar.edu.um.montana.cars.repository.PurchasedCarRepository;
import com.ar.edu.um.montana.cars.service.dto.PurchasedCarDTO;
import com.ar.edu.um.montana.cars.service.mapper.PurchasedCarMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PurchasedCarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchasedCarResourceIT {

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PURCHASE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/purchased-cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PurchasedCarRepository purchasedCarRepository;

    @Autowired
    private PurchasedCarMapper purchasedCarMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchasedCarMockMvc;

    private PurchasedCar purchasedCar;

    private PurchasedCar insertedPurchasedCar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedCar createEntity() {
        return new PurchasedCar().purchaseDate(DEFAULT_PURCHASE_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedCar createUpdatedEntity() {
        return new PurchasedCar().purchaseDate(UPDATED_PURCHASE_DATE);
    }

    @BeforeEach
    public void initTest() {
        purchasedCar = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPurchasedCar != null) {
            purchasedCarRepository.delete(insertedPurchasedCar);
            insertedPurchasedCar = null;
        }
    }

    @Test
    @Transactional
    void createPurchasedCar() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);
        var returnedPurchasedCarDTO = om.readValue(
            restPurchasedCarMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedCarDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PurchasedCarDTO.class
        );

        // Validate the PurchasedCar in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPurchasedCar = purchasedCarMapper.toEntity(returnedPurchasedCarDTO);
        assertPurchasedCarUpdatableFieldsEquals(returnedPurchasedCar, getPersistedPurchasedCar(returnedPurchasedCar));

        insertedPurchasedCar = returnedPurchasedCar;
    }

    @Test
    @Transactional
    void createPurchasedCarWithExistingId() throws Exception {
        // Create the PurchasedCar with an existing ID
        purchasedCar.setId(1L);
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchasedCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedCarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchasedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPurchasedCars() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList
        restPurchasedCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasedCar.getId().intValue())))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())));
    }

    @Test
    @Transactional
    void getPurchasedCar() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get the purchasedCar
        restPurchasedCarMockMvc
            .perform(get(ENTITY_API_URL_ID, purchasedCar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchasedCar.getId().intValue()))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()));
    }

    @Test
    @Transactional
    void getPurchasedCarsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        Long id = purchasedCar.getId();

        defaultPurchasedCarFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPurchasedCarFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPurchasedCarFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate equals to
        defaultPurchasedCarFiltering("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE, "purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate in
        defaultPurchasedCarFiltering(
            "purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE,
            "purchaseDate.in=" + UPDATED_PURCHASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate is not null
        defaultPurchasedCarFiltering("purchaseDate.specified=true", "purchaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate is greater than or equal to
        defaultPurchasedCarFiltering(
            "purchaseDate.greaterThanOrEqual=" + DEFAULT_PURCHASE_DATE,
            "purchaseDate.greaterThanOrEqual=" + UPDATED_PURCHASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate is less than or equal to
        defaultPurchasedCarFiltering(
            "purchaseDate.lessThanOrEqual=" + DEFAULT_PURCHASE_DATE,
            "purchaseDate.lessThanOrEqual=" + SMALLER_PURCHASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate is less than
        defaultPurchasedCarFiltering("purchaseDate.lessThan=" + UPDATED_PURCHASE_DATE, "purchaseDate.lessThan=" + DEFAULT_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate is greater than
        defaultPurchasedCarFiltering(
            "purchaseDate.greaterThan=" + SMALLER_PURCHASE_DATE,
            "purchaseDate.greaterThan=" + DEFAULT_PURCHASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByCarIsEqualToSomething() throws Exception {
        Car car;
        if (TestUtil.findAll(em, Car.class).isEmpty()) {
            purchasedCarRepository.saveAndFlush(purchasedCar);
            car = CarResourceIT.createEntity();
        } else {
            car = TestUtil.findAll(em, Car.class).get(0);
        }
        em.persist(car);
        em.flush();
        purchasedCar.setCar(car);
        purchasedCarRepository.saveAndFlush(purchasedCar);
        Long carId = car.getId();
        // Get all the purchasedCarList where car equals to carId
        defaultPurchasedCarShouldBeFound("carId.equals=" + carId);

        // Get all the purchasedCarList where car equals to (carId + 1)
        defaultPurchasedCarShouldNotBeFound("carId.equals=" + (carId + 1));
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            purchasedCarRepository.saveAndFlush(purchasedCar);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        purchasedCar.setClient(client);
        purchasedCarRepository.saveAndFlush(purchasedCar);
        Long clientId = client.getId();
        // Get all the purchasedCarList where client equals to clientId
        defaultPurchasedCarShouldBeFound("clientId.equals=" + clientId);

        // Get all the purchasedCarList where client equals to (clientId + 1)
        defaultPurchasedCarShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    private void defaultPurchasedCarFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPurchasedCarShouldBeFound(shouldBeFound);
        defaultPurchasedCarShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPurchasedCarShouldBeFound(String filter) throws Exception {
        restPurchasedCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasedCar.getId().intValue())))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())));

        // Check, that the count call also returns 1
        restPurchasedCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPurchasedCarShouldNotBeFound(String filter) throws Exception {
        restPurchasedCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchasedCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPurchasedCar() throws Exception {
        // Get the purchasedCar
        restPurchasedCarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchasedCar() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedCar
        PurchasedCar updatedPurchasedCar = purchasedCarRepository.findById(purchasedCar.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPurchasedCar are not directly saved in db
        em.detach(updatedPurchasedCar);
        updatedPurchasedCar.purchaseDate(UPDATED_PURCHASE_DATE);
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(updatedPurchasedCar);

        restPurchasedCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedCarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchasedCarDTO))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPurchasedCarToMatchAllProperties(updatedPurchasedCar);
    }

    @Test
    @Transactional
    void putNonExistingPurchasedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedCar.setId(longCount.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedCarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchasedCarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchasedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedCar.setId(longCount.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchasedCarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchasedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedCar.setId(longCount.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedCarDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchasedCarWithPatch() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedCar using partial update
        PurchasedCar partialUpdatedPurchasedCar = new PurchasedCar();
        partialUpdatedPurchasedCar.setId(purchasedCar.getId());

        partialUpdatedPurchasedCar.purchaseDate(UPDATED_PURCHASE_DATE);

        restPurchasedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchasedCar))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedCar in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchasedCarUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPurchasedCar, purchasedCar),
            getPersistedPurchasedCar(purchasedCar)
        );
    }

    @Test
    @Transactional
    void fullUpdatePurchasedCarWithPatch() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedCar using partial update
        PurchasedCar partialUpdatedPurchasedCar = new PurchasedCar();
        partialUpdatedPurchasedCar.setId(purchasedCar.getId());

        partialUpdatedPurchasedCar.purchaseDate(UPDATED_PURCHASE_DATE);

        restPurchasedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchasedCar))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedCar in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchasedCarUpdatableFieldsEquals(partialUpdatedPurchasedCar, getPersistedPurchasedCar(partialUpdatedPurchasedCar));
    }

    @Test
    @Transactional
    void patchNonExistingPurchasedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedCar.setId(longCount.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchasedCarDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchasedCarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchasedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedCar.setId(longCount.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchasedCarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchasedCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedCar.setId(longCount.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(purchasedCarDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedCar in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchasedCar() throws Exception {
        // Initialize the database
        insertedPurchasedCar = purchasedCarRepository.saveAndFlush(purchasedCar);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the purchasedCar
        restPurchasedCarMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchasedCar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return purchasedCarRepository.count();
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

    protected PurchasedCar getPersistedPurchasedCar(PurchasedCar purchasedCar) {
        return purchasedCarRepository.findById(purchasedCar.getId()).orElseThrow();
    }

    protected void assertPersistedPurchasedCarToMatchAllProperties(PurchasedCar expectedPurchasedCar) {
        assertPurchasedCarAllPropertiesEquals(expectedPurchasedCar, getPersistedPurchasedCar(expectedPurchasedCar));
    }

    protected void assertPersistedPurchasedCarToMatchUpdatableProperties(PurchasedCar expectedPurchasedCar) {
        assertPurchasedCarAllUpdatablePropertiesEquals(expectedPurchasedCar, getPersistedPurchasedCar(expectedPurchasedCar));
    }
}
