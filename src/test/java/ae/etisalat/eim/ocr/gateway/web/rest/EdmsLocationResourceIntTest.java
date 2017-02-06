package ae.etisalat.eim.ocr.gateway.web.rest;

import ae.etisalat.eim.ocr.gateway.OcrGatewayApp;

import ae.etisalat.eim.ocr.gateway.domain.EdmsLocation;
import ae.etisalat.eim.ocr.gateway.repository.EdmsLocationRepository;
import ae.etisalat.eim.ocr.gateway.service.EdmsLocationService;
import ae.etisalat.eim.ocr.gateway.repository.search.EdmsLocationSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsLocationDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.EdmsLocationMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EdmsLocationResource REST controller.
 *
 * @see EdmsLocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OcrGatewayApp.class)
public class EdmsLocationResourceIntTest {

    private static final String DEFAULT_ACTUAL_DIRECTORY = "AAAAAAAAAA";
    private static final String UPDATED_ACTUAL_DIRECTORY = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS_ID = 1;
    private static final Integer UPDATED_STATUS_ID = 2;

    @Inject
    private EdmsLocationRepository edmsLocationRepository;

    @Inject
    private EdmsLocationMapper edmsLocationMapper;

    @Inject
    private EdmsLocationService edmsLocationService;

    @Inject
    private EdmsLocationSearchRepository edmsLocationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEdmsLocationMockMvc;

    private EdmsLocation edmsLocation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EdmsLocationResource edmsLocationResource = new EdmsLocationResource();
        ReflectionTestUtils.setField(edmsLocationResource, "edmsLocationService", edmsLocationService);
        this.restEdmsLocationMockMvc = MockMvcBuilders.standaloneSetup(edmsLocationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EdmsLocation createEntity(EntityManager em) {
        EdmsLocation edmsLocation = new EdmsLocation()
                .actualDirectory(DEFAULT_ACTUAL_DIRECTORY)
                .statusId(DEFAULT_STATUS_ID);
        return edmsLocation;
    }

    @Before
    public void initTest() {
        edmsLocationSearchRepository.deleteAll();
        edmsLocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createEdmsLocation() throws Exception {
        int databaseSizeBeforeCreate = edmsLocationRepository.findAll().size();

        // Create the EdmsLocation
        EdmsLocationDTO edmsLocationDTO = edmsLocationMapper.edmsLocationToEdmsLocationDTO(edmsLocation);

        restEdmsLocationMockMvc.perform(post("/api/edms-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsLocationDTO)))
            .andExpect(status().isCreated());

        // Validate the EdmsLocation in the database
        List<EdmsLocation> edmsLocationList = edmsLocationRepository.findAll();
        assertThat(edmsLocationList).hasSize(databaseSizeBeforeCreate + 1);
        EdmsLocation testEdmsLocation = edmsLocationList.get(edmsLocationList.size() - 1);
        assertThat(testEdmsLocation.getActualDirectory()).isEqualTo(DEFAULT_ACTUAL_DIRECTORY);
        assertThat(testEdmsLocation.getStatusId()).isEqualTo(DEFAULT_STATUS_ID);

        // Validate the EdmsLocation in ElasticSearch
        EdmsLocation edmsLocationEs = edmsLocationSearchRepository.findOne(testEdmsLocation.getId());
        assertThat(edmsLocationEs).isEqualToComparingFieldByField(testEdmsLocation);
    }

    @Test
    @Transactional
    public void createEdmsLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = edmsLocationRepository.findAll().size();

        // Create the EdmsLocation with an existing ID
        EdmsLocation existingEdmsLocation = new EdmsLocation();
        existingEdmsLocation.setId(1L);
        EdmsLocationDTO existingEdmsLocationDTO = edmsLocationMapper.edmsLocationToEdmsLocationDTO(existingEdmsLocation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEdmsLocationMockMvc.perform(post("/api/edms-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEdmsLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EdmsLocation> edmsLocationList = edmsLocationRepository.findAll();
        assertThat(edmsLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkActualDirectoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = edmsLocationRepository.findAll().size();
        // set the field null
        edmsLocation.setActualDirectory(null);

        // Create the EdmsLocation, which fails.
        EdmsLocationDTO edmsLocationDTO = edmsLocationMapper.edmsLocationToEdmsLocationDTO(edmsLocation);

        restEdmsLocationMockMvc.perform(post("/api/edms-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsLocationDTO)))
            .andExpect(status().isBadRequest());

        List<EdmsLocation> edmsLocationList = edmsLocationRepository.findAll();
        assertThat(edmsLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = edmsLocationRepository.findAll().size();
        // set the field null
        edmsLocation.setStatusId(null);

        // Create the EdmsLocation, which fails.
        EdmsLocationDTO edmsLocationDTO = edmsLocationMapper.edmsLocationToEdmsLocationDTO(edmsLocation);

        restEdmsLocationMockMvc.perform(post("/api/edms-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsLocationDTO)))
            .andExpect(status().isBadRequest());

        List<EdmsLocation> edmsLocationList = edmsLocationRepository.findAll();
        assertThat(edmsLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEdmsLocations() throws Exception {
        // Initialize the database
        edmsLocationRepository.saveAndFlush(edmsLocation);

        // Get all the edmsLocationList
        restEdmsLocationMockMvc.perform(get("/api/edms-locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edmsLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualDirectory").value(hasItem(DEFAULT_ACTUAL_DIRECTORY.toString())))
            .andExpect(jsonPath("$.[*].statusId").value(hasItem(DEFAULT_STATUS_ID)));
    }

    @Test
    @Transactional
    public void getEdmsLocation() throws Exception {
        // Initialize the database
        edmsLocationRepository.saveAndFlush(edmsLocation);

        // Get the edmsLocation
        restEdmsLocationMockMvc.perform(get("/api/edms-locations/{id}", edmsLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(edmsLocation.getId().intValue()))
            .andExpect(jsonPath("$.actualDirectory").value(DEFAULT_ACTUAL_DIRECTORY.toString()))
            .andExpect(jsonPath("$.statusId").value(DEFAULT_STATUS_ID));
    }

    @Test
    @Transactional
    public void getNonExistingEdmsLocation() throws Exception {
        // Get the edmsLocation
        restEdmsLocationMockMvc.perform(get("/api/edms-locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEdmsLocation() throws Exception {
        // Initialize the database
        edmsLocationRepository.saveAndFlush(edmsLocation);
        edmsLocationSearchRepository.save(edmsLocation);
        int databaseSizeBeforeUpdate = edmsLocationRepository.findAll().size();

        // Update the edmsLocation
        EdmsLocation updatedEdmsLocation = edmsLocationRepository.findOne(edmsLocation.getId());
        updatedEdmsLocation
                .actualDirectory(UPDATED_ACTUAL_DIRECTORY)
                .statusId(UPDATED_STATUS_ID);
        EdmsLocationDTO edmsLocationDTO = edmsLocationMapper.edmsLocationToEdmsLocationDTO(updatedEdmsLocation);

        restEdmsLocationMockMvc.perform(put("/api/edms-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsLocationDTO)))
            .andExpect(status().isOk());

        // Validate the EdmsLocation in the database
        List<EdmsLocation> edmsLocationList = edmsLocationRepository.findAll();
        assertThat(edmsLocationList).hasSize(databaseSizeBeforeUpdate);
        EdmsLocation testEdmsLocation = edmsLocationList.get(edmsLocationList.size() - 1);
        assertThat(testEdmsLocation.getActualDirectory()).isEqualTo(UPDATED_ACTUAL_DIRECTORY);
        assertThat(testEdmsLocation.getStatusId()).isEqualTo(UPDATED_STATUS_ID);

        // Validate the EdmsLocation in ElasticSearch
        EdmsLocation edmsLocationEs = edmsLocationSearchRepository.findOne(testEdmsLocation.getId());
        assertThat(edmsLocationEs).isEqualToComparingFieldByField(testEdmsLocation);
    }

    @Test
    @Transactional
    public void updateNonExistingEdmsLocation() throws Exception {
        int databaseSizeBeforeUpdate = edmsLocationRepository.findAll().size();

        // Create the EdmsLocation
        EdmsLocationDTO edmsLocationDTO = edmsLocationMapper.edmsLocationToEdmsLocationDTO(edmsLocation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEdmsLocationMockMvc.perform(put("/api/edms-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsLocationDTO)))
            .andExpect(status().isCreated());

        // Validate the EdmsLocation in the database
        List<EdmsLocation> edmsLocationList = edmsLocationRepository.findAll();
        assertThat(edmsLocationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEdmsLocation() throws Exception {
        // Initialize the database
        edmsLocationRepository.saveAndFlush(edmsLocation);
        edmsLocationSearchRepository.save(edmsLocation);
        int databaseSizeBeforeDelete = edmsLocationRepository.findAll().size();

        // Get the edmsLocation
        restEdmsLocationMockMvc.perform(delete("/api/edms-locations/{id}", edmsLocation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean edmsLocationExistsInEs = edmsLocationSearchRepository.exists(edmsLocation.getId());
        assertThat(edmsLocationExistsInEs).isFalse();

        // Validate the database is empty
        List<EdmsLocation> edmsLocationList = edmsLocationRepository.findAll();
        assertThat(edmsLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEdmsLocation() throws Exception {
        // Initialize the database
        edmsLocationRepository.saveAndFlush(edmsLocation);
        edmsLocationSearchRepository.save(edmsLocation);

        // Search the edmsLocation
        restEdmsLocationMockMvc.perform(get("/api/_search/edms-locations?query=id:" + edmsLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edmsLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualDirectory").value(hasItem(DEFAULT_ACTUAL_DIRECTORY.toString())))
            .andExpect(jsonPath("$.[*].statusId").value(hasItem(DEFAULT_STATUS_ID)));
    }
}
