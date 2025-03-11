package com.ar.edu.um.taccetta.cars.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ar.edu.um.taccetta.cars.IntegrationTest;
import com.ar.edu.um.taccetta.cars.domain.Car;
import com.ar.edu.um.taccetta.cars.domain.Client;
import com.ar.edu.um.taccetta.cars.domain.PurchasedCar;
import com.ar.edu.um.taccetta.cars.repository.PurchasedCarRepository;
import com.ar.edu.um.taccetta.cars.service.PurchasedCarService;
import com.ar.edu.um.taccetta.cars.service.criteria.PurchasedCarCriteria;
import com.ar.edu.um.taccetta.cars.service.dto.PurchasedCarDTO;
import com.ar.edu.um.taccetta.cars.service.mapper.PurchasedCarMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PurchasedCarResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PurchasedCarResourceIT {

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PURCHASE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/purchased-cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchasedCarRepository purchasedCarRepository;

    @Mock
    private PurchasedCarRepository purchasedCarRepositoryMock;

    @Autowired
    private PurchasedCarMapper purchasedCarMapper;

    @Mock
    private PurchasedCarService purchasedCarServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchasedCarMockMvc;

    private PurchasedCar purchasedCar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedCar createEntity(EntityManager em) {
        PurchasedCar purchasedCar = new PurchasedCar().purchaseDate(DEFAULT_PURCHASE_DATE);
        return purchasedCar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedCar createUpdatedEntity(EntityManager em) {
        PurchasedCar purchasedCar = new PurchasedCar().purchaseDate(UPDATED_PURCHASE_DATE);
        return purchasedCar;
    }

    @BeforeEach
    public void initTest() {
        purchasedCar = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchasedCar() throws Exception {
        int databaseSizeBeforeCreate = purchasedCarRepository.findAll().size();
        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);
        restPurchasedCarMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedCarDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PurchasedCar in the database
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeCreate + 1);
        PurchasedCar testPurchasedCar = purchasedCarList.get(purchasedCarList.size() - 1);
        assertThat(testPurchasedCar.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void createPurchasedCarWithExistingId() throws Exception {
        // Create the PurchasedCar with an existing ID
        purchasedCar.setId(1L);
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        int databaseSizeBeforeCreate = purchasedCarRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchasedCarMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedCarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedCar in the database
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPurchasedCars() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList
        restPurchasedCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasedCar.getId().intValue())))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedCarsWithEagerRelationshipsIsEnabled() throws Exception {
        when(purchasedCarServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchasedCarMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(purchasedCarServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedCarsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(purchasedCarServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchasedCarMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(purchasedCarRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPurchasedCar() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

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
        purchasedCarRepository.saveAndFlush(purchasedCar);

        Long id = purchasedCar.getId();

        defaultPurchasedCarShouldBeFound("id.equals=" + id);
        defaultPurchasedCarShouldNotBeFound("id.notEquals=" + id);

        defaultPurchasedCarShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPurchasedCarShouldNotBeFound("id.greaterThan=" + id);

        defaultPurchasedCarShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPurchasedCarShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate equals to DEFAULT_PURCHASE_DATE
        defaultPurchasedCarShouldBeFound("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE);

        // Get all the purchasedCarList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultPurchasedCarShouldNotBeFound("purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate in DEFAULT_PURCHASE_DATE or UPDATED_PURCHASE_DATE
        defaultPurchasedCarShouldBeFound("purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE);

        // Get all the purchasedCarList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultPurchasedCarShouldNotBeFound("purchaseDate.in=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate is not null
        defaultPurchasedCarShouldBeFound("purchaseDate.specified=true");

        // Get all the purchasedCarList where purchaseDate is null
        defaultPurchasedCarShouldNotBeFound("purchaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate is greater than or equal to DEFAULT_PURCHASE_DATE
        defaultPurchasedCarShouldBeFound("purchaseDate.greaterThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the purchasedCarList where purchaseDate is greater than or equal to UPDATED_PURCHASE_DATE
        defaultPurchasedCarShouldNotBeFound("purchaseDate.greaterThanOrEqual=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate is less than or equal to DEFAULT_PURCHASE_DATE
        defaultPurchasedCarShouldBeFound("purchaseDate.lessThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the purchasedCarList where purchaseDate is less than or equal to SMALLER_PURCHASE_DATE
        defaultPurchasedCarShouldNotBeFound("purchaseDate.lessThanOrEqual=" + SMALLER_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate is less than DEFAULT_PURCHASE_DATE
        defaultPurchasedCarShouldNotBeFound("purchaseDate.lessThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the purchasedCarList where purchaseDate is less than UPDATED_PURCHASE_DATE
        defaultPurchasedCarShouldBeFound("purchaseDate.lessThan=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByPurchaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

        // Get all the purchasedCarList where purchaseDate is greater than DEFAULT_PURCHASE_DATE
        defaultPurchasedCarShouldNotBeFound("purchaseDate.greaterThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the purchasedCarList where purchaseDate is greater than SMALLER_PURCHASE_DATE
        defaultPurchasedCarShouldBeFound("purchaseDate.greaterThan=" + SMALLER_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllPurchasedCarsByCarIsEqualToSomething() throws Exception {
        Car car;
        if (TestUtil.findAll(em, Car.class).isEmpty()) {
            purchasedCarRepository.saveAndFlush(purchasedCar);
            car = CarResourceIT.createEntity(em);
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
            client = ClientResourceIT.createEntity(em);
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
        purchasedCarRepository.saveAndFlush(purchasedCar);

        int databaseSizeBeforeUpdate = purchasedCarRepository.findAll().size();

        // Update the purchasedCar
        PurchasedCar updatedPurchasedCar = purchasedCarRepository.findById(purchasedCar.getId()).get();
        // Disconnect from session so that the updates on updatedPurchasedCar are not directly saved in db
        em.detach(updatedPurchasedCar);
        updatedPurchasedCar.purchaseDate(UPDATED_PURCHASE_DATE);
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(updatedPurchasedCar);

        restPurchasedCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedCarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedCarDTO))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedCar in the database
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeUpdate);
        PurchasedCar testPurchasedCar = purchasedCarList.get(purchasedCarList.size() - 1);
        assertThat(testPurchasedCar.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPurchasedCar() throws Exception {
        int databaseSizeBeforeUpdate = purchasedCarRepository.findAll().size();
        purchasedCar.setId(count.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedCarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedCarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedCar in the database
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchasedCar() throws Exception {
        int databaseSizeBeforeUpdate = purchasedCarRepository.findAll().size();
        purchasedCar.setId(count.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedCarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedCar in the database
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchasedCar() throws Exception {
        int databaseSizeBeforeUpdate = purchasedCarRepository.findAll().size();
        purchasedCar.setId(count.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedCarDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedCar in the database
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchasedCarWithPatch() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

        int databaseSizeBeforeUpdate = purchasedCarRepository.findAll().size();

        // Update the purchasedCar using partial update
        PurchasedCar partialUpdatedPurchasedCar = new PurchasedCar();
        partialUpdatedPurchasedCar.setId(purchasedCar.getId());

        partialUpdatedPurchasedCar.purchaseDate(UPDATED_PURCHASE_DATE);

        restPurchasedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedCar))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedCar in the database
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeUpdate);
        PurchasedCar testPurchasedCar = purchasedCarList.get(purchasedCarList.size() - 1);
        assertThat(testPurchasedCar.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePurchasedCarWithPatch() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

        int databaseSizeBeforeUpdate = purchasedCarRepository.findAll().size();

        // Update the purchasedCar using partial update
        PurchasedCar partialUpdatedPurchasedCar = new PurchasedCar();
        partialUpdatedPurchasedCar.setId(purchasedCar.getId());

        partialUpdatedPurchasedCar.purchaseDate(UPDATED_PURCHASE_DATE);

        restPurchasedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedCar))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedCar in the database
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeUpdate);
        PurchasedCar testPurchasedCar = purchasedCarList.get(purchasedCarList.size() - 1);
        assertThat(testPurchasedCar.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPurchasedCar() throws Exception {
        int databaseSizeBeforeUpdate = purchasedCarRepository.findAll().size();
        purchasedCar.setId(count.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchasedCarDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedCarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedCar in the database
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchasedCar() throws Exception {
        int databaseSizeBeforeUpdate = purchasedCarRepository.findAll().size();
        purchasedCar.setId(count.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedCarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedCar in the database
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchasedCar() throws Exception {
        int databaseSizeBeforeUpdate = purchasedCarRepository.findAll().size();
        purchasedCar.setId(count.incrementAndGet());

        // Create the PurchasedCar
        PurchasedCarDTO purchasedCarDTO = purchasedCarMapper.toDto(purchasedCar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedCarMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedCarDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedCar in the database
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchasedCar() throws Exception {
        // Initialize the database
        purchasedCarRepository.saveAndFlush(purchasedCar);

        int databaseSizeBeforeDelete = purchasedCarRepository.findAll().size();

        // Delete the purchasedCar
        restPurchasedCarMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchasedCar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchasedCar> purchasedCarList = purchasedCarRepository.findAll();
        assertThat(purchasedCarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
