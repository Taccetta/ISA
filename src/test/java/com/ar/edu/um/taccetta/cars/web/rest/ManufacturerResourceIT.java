package com.ar.edu.um.taccetta.cars.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ar.edu.um.taccetta.cars.IntegrationTest;
import com.ar.edu.um.taccetta.cars.domain.Manufacturer;
import com.ar.edu.um.taccetta.cars.repository.ManufacturerRepository;
import com.ar.edu.um.taccetta.cars.service.criteria.ManufacturerCriteria;
import com.ar.edu.um.taccetta.cars.service.dto.ManufacturerDTO;
import com.ar.edu.um.taccetta.cars.service.mapper.ManufacturerMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ManufacturerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManufacturerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/manufacturers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManufacturerMockMvc;

    private Manufacturer manufacturer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manufacturer createEntity(EntityManager em) {
        Manufacturer manufacturer = new Manufacturer().name(DEFAULT_NAME);
        return manufacturer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manufacturer createUpdatedEntity(EntityManager em) {
        Manufacturer manufacturer = new Manufacturer().name(UPDATED_NAME);
        return manufacturer;
    }

    @BeforeEach
    public void initTest() {
        manufacturer = createEntity(em);
    }

    @Test
    @Transactional
    void createManufacturer() throws Exception {
        int databaseSizeBeforeCreate = manufacturerRepository.findAll().size();
        // Create the Manufacturer
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);
        restManufacturerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manufacturerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeCreate + 1);
        Manufacturer testManufacturer = manufacturerList.get(manufacturerList.size() - 1);
        assertThat(testManufacturer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createManufacturerWithExistingId() throws Exception {
        // Create the Manufacturer with an existing ID
        manufacturer.setId(1L);
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);

        int databaseSizeBeforeCreate = manufacturerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManufacturerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manufacturerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = manufacturerRepository.findAll().size();
        // set the field null
        manufacturer.setName(null);

        // Create the Manufacturer, which fails.
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);

        restManufacturerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manufacturerDTO))
            )
            .andExpect(status().isBadRequest());

        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllManufacturers() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList
        restManufacturerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manufacturer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getManufacturer() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get the manufacturer
        restManufacturerMockMvc
            .perform(get(ENTITY_API_URL_ID, manufacturer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manufacturer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getManufacturersByIdFiltering() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        Long id = manufacturer.getId();

        defaultManufacturerShouldBeFound("id.equals=" + id);
        defaultManufacturerShouldNotBeFound("id.notEquals=" + id);

        defaultManufacturerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultManufacturerShouldNotBeFound("id.greaterThan=" + id);

        defaultManufacturerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultManufacturerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllManufacturersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where name equals to DEFAULT_NAME
        defaultManufacturerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the manufacturerList where name equals to UPDATED_NAME
        defaultManufacturerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllManufacturersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultManufacturerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the manufacturerList where name equals to UPDATED_NAME
        defaultManufacturerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllManufacturersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where name is not null
        defaultManufacturerShouldBeFound("name.specified=true");

        // Get all the manufacturerList where name is null
        defaultManufacturerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllManufacturersByNameContainsSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where name contains DEFAULT_NAME
        defaultManufacturerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the manufacturerList where name contains UPDATED_NAME
        defaultManufacturerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllManufacturersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where name does not contain DEFAULT_NAME
        defaultManufacturerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the manufacturerList where name does not contain UPDATED_NAME
        defaultManufacturerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultManufacturerShouldBeFound(String filter) throws Exception {
        restManufacturerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manufacturer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restManufacturerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultManufacturerShouldNotBeFound(String filter) throws Exception {
        restManufacturerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restManufacturerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingManufacturer() throws Exception {
        // Get the manufacturer
        restManufacturerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingManufacturer() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();

        // Update the manufacturer
        Manufacturer updatedManufacturer = manufacturerRepository.findById(manufacturer.getId()).get();
        // Disconnect from session so that the updates on updatedManufacturer are not directly saved in db
        em.detach(updatedManufacturer);
        updatedManufacturer.name(UPDATED_NAME);
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(updatedManufacturer);

        restManufacturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manufacturerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manufacturerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeUpdate);
        Manufacturer testManufacturer = manufacturerList.get(manufacturerList.size() - 1);
        assertThat(testManufacturer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingManufacturer() throws Exception {
        int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();
        manufacturer.setId(count.incrementAndGet());

        // Create the Manufacturer
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manufacturerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manufacturerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManufacturer() throws Exception {
        int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();
        manufacturer.setId(count.incrementAndGet());

        // Create the Manufacturer
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manufacturerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManufacturer() throws Exception {
        int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();
        manufacturer.setId(count.incrementAndGet());

        // Create the Manufacturer
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manufacturerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManufacturerWithPatch() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();

        // Update the manufacturer using partial update
        Manufacturer partialUpdatedManufacturer = new Manufacturer();
        partialUpdatedManufacturer.setId(manufacturer.getId());

        partialUpdatedManufacturer.name(UPDATED_NAME);

        restManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManufacturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManufacturer))
            )
            .andExpect(status().isOk());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeUpdate);
        Manufacturer testManufacturer = manufacturerList.get(manufacturerList.size() - 1);
        assertThat(testManufacturer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateManufacturerWithPatch() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();

        // Update the manufacturer using partial update
        Manufacturer partialUpdatedManufacturer = new Manufacturer();
        partialUpdatedManufacturer.setId(manufacturer.getId());

        partialUpdatedManufacturer.name(UPDATED_NAME);

        restManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManufacturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManufacturer))
            )
            .andExpect(status().isOk());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeUpdate);
        Manufacturer testManufacturer = manufacturerList.get(manufacturerList.size() - 1);
        assertThat(testManufacturer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingManufacturer() throws Exception {
        int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();
        manufacturer.setId(count.incrementAndGet());

        // Create the Manufacturer
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manufacturerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manufacturerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManufacturer() throws Exception {
        int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();
        manufacturer.setId(count.incrementAndGet());

        // Create the Manufacturer
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manufacturerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManufacturer() throws Exception {
        int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();
        manufacturer.setId(count.incrementAndGet());

        // Create the Manufacturer
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manufacturerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManufacturer() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        int databaseSizeBeforeDelete = manufacturerRepository.findAll().size();

        // Delete the manufacturer
        restManufacturerMockMvc
            .perform(delete(ENTITY_API_URL_ID, manufacturer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
