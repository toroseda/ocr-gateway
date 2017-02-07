package ae.etisalat.eim.ocr.gateway.web.rest;

import ae.etisalat.eim.ocr.gateway.OcrGatewayApp;

import ae.etisalat.eim.ocr.gateway.domain.EdmsDownload;
import ae.etisalat.eim.ocr.gateway.repository.EdmsDownloadRepository;
import ae.etisalat.eim.ocr.gateway.service.EdmsDownloadService;
import ae.etisalat.eim.ocr.gateway.repository.search.EdmsDownloadSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsDownloadDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.EdmsDownloadMapper;

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
 * Test class for the EdmsDownloadResource REST controller.
 *
 * @see EdmsDownloadResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OcrGatewayApp.class)
public class EdmsDownloadResourceIntTest {

    private static final String DEFAULT_ACTUAL_DIRECTORY = "AAAAAAAAAA";
    private static final String UPDATED_ACTUAL_DIRECTORY = "BBBBBBBBBB";

    private static final String DEFAULT_ACTUAL_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_ACTUAL_FILENAME = "BBBBBBBBBB";

    @Inject
    private EdmsDownloadRepository edmsDownloadRepository;

    @Inject
    private EdmsDownloadMapper edmsDownloadMapper;

    @Inject
    private EdmsDownloadService edmsDownloadService;

    @Inject
    private EdmsDownloadSearchRepository edmsDownloadSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEdmsDownloadMockMvc;

    private EdmsDownload edmsDownload;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EdmsDownloadResource edmsDownloadResource = new EdmsDownloadResource();
        ReflectionTestUtils.setField(edmsDownloadResource, "edmsDownloadService", edmsDownloadService);
        this.restEdmsDownloadMockMvc = MockMvcBuilders.standaloneSetup(edmsDownloadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EdmsDownload createEntity(EntityManager em) {
        EdmsDownload edmsDownload = new EdmsDownload()
                .actualDirectory(DEFAULT_ACTUAL_DIRECTORY)
                .actualFilename(DEFAULT_ACTUAL_FILENAME);
        return edmsDownload;
    }

    @Before
    public void initTest() {
        edmsDownloadSearchRepository.deleteAll();
        edmsDownload = createEntity(em);
    }

    @Test
    @Transactional
    public void createEdmsDownload() throws Exception {
        int databaseSizeBeforeCreate = edmsDownloadRepository.findAll().size();

        // Create the EdmsDownload
        EdmsDownloadDTO edmsDownloadDTO = edmsDownloadMapper.edmsDownloadToEdmsDownloadDTO(edmsDownload);

        restEdmsDownloadMockMvc.perform(post("/api/edms-downloads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsDownloadDTO)))
            .andExpect(status().isCreated());

        // Validate the EdmsDownload in the database
        List<EdmsDownload> edmsDownloadList = edmsDownloadRepository.findAll();
        assertThat(edmsDownloadList).hasSize(databaseSizeBeforeCreate + 1);
        EdmsDownload testEdmsDownload = edmsDownloadList.get(edmsDownloadList.size() - 1);
        assertThat(testEdmsDownload.getActualDirectory()).isEqualTo(DEFAULT_ACTUAL_DIRECTORY);
        assertThat(testEdmsDownload.getActualFilename()).isEqualTo(DEFAULT_ACTUAL_FILENAME);

        // Validate the EdmsDownload in ElasticSearch
        /*EdmsDownload edmsDownloadEs = edmsDownloadSearchRepository.findOne(testEdmsDownload.getId());
        assertThat(edmsDownloadEs).isEqualToComparingFieldByField(testEdmsDownload);*/
    }

    @Test
    @Transactional
    public void createEdmsDownloadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = edmsDownloadRepository.findAll().size();

        // Create the EdmsDownload with an existing ID
        EdmsDownload existingEdmsDownload = new EdmsDownload();
        existingEdmsDownload.setId(1L);
        EdmsDownloadDTO existingEdmsDownloadDTO = edmsDownloadMapper.edmsDownloadToEdmsDownloadDTO(existingEdmsDownload);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEdmsDownloadMockMvc.perform(post("/api/edms-downloads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEdmsDownloadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EdmsDownload> edmsDownloadList = edmsDownloadRepository.findAll();
        assertThat(edmsDownloadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkActualDirectoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = edmsDownloadRepository.findAll().size();
        // set the field null
        edmsDownload.setActualDirectory(null);

        // Create the EdmsDownload, which fails.
        EdmsDownloadDTO edmsDownloadDTO = edmsDownloadMapper.edmsDownloadToEdmsDownloadDTO(edmsDownload);

        restEdmsDownloadMockMvc.perform(post("/api/edms-downloads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsDownloadDTO)))
            .andExpect(status().isBadRequest());

        List<EdmsDownload> edmsDownloadList = edmsDownloadRepository.findAll();
        assertThat(edmsDownloadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActualFilenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = edmsDownloadRepository.findAll().size();
        // set the field null
        edmsDownload.setActualFilename(null);

        // Create the EdmsDownload, which fails.
        EdmsDownloadDTO edmsDownloadDTO = edmsDownloadMapper.edmsDownloadToEdmsDownloadDTO(edmsDownload);

        restEdmsDownloadMockMvc.perform(post("/api/edms-downloads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsDownloadDTO)))
            .andExpect(status().isBadRequest());

        List<EdmsDownload> edmsDownloadList = edmsDownloadRepository.findAll();
        assertThat(edmsDownloadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEdmsDownloads() throws Exception {
        // Initialize the database
        edmsDownloadRepository.saveAndFlush(edmsDownload);

        // Get all the edmsDownloadList
        restEdmsDownloadMockMvc.perform(get("/api/edms-downloads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edmsDownload.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualDirectory").value(hasItem(DEFAULT_ACTUAL_DIRECTORY.toString())))
            .andExpect(jsonPath("$.[*].actualFilename").value(hasItem(DEFAULT_ACTUAL_FILENAME.toString())));
    }

    @Test
    @Transactional
    public void getEdmsDownload() throws Exception {
        // Initialize the database
        edmsDownloadRepository.saveAndFlush(edmsDownload);

        // Get the edmsDownload
        restEdmsDownloadMockMvc.perform(get("/api/edms-downloads/{id}", edmsDownload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(edmsDownload.getId().intValue()))
            .andExpect(jsonPath("$.actualDirectory").value(DEFAULT_ACTUAL_DIRECTORY.toString()))
            .andExpect(jsonPath("$.actualFilename").value(DEFAULT_ACTUAL_FILENAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEdmsDownload() throws Exception {
        // Get the edmsDownload
        restEdmsDownloadMockMvc.perform(get("/api/edms-downloads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEdmsDownload() throws Exception {
        // Initialize the database
        edmsDownloadRepository.saveAndFlush(edmsDownload);
        edmsDownloadSearchRepository.save(edmsDownload);
        int databaseSizeBeforeUpdate = edmsDownloadRepository.findAll().size();

        // Update the edmsDownload
        EdmsDownload updatedEdmsDownload = edmsDownloadRepository.findOne(edmsDownload.getId());
        updatedEdmsDownload
                .actualDirectory(UPDATED_ACTUAL_DIRECTORY)
                .actualFilename(UPDATED_ACTUAL_FILENAME);
        EdmsDownloadDTO edmsDownloadDTO = edmsDownloadMapper.edmsDownloadToEdmsDownloadDTO(updatedEdmsDownload);

        restEdmsDownloadMockMvc.perform(put("/api/edms-downloads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsDownloadDTO)))
            .andExpect(status().isOk());

        // Validate the EdmsDownload in the database
        List<EdmsDownload> edmsDownloadList = edmsDownloadRepository.findAll();
        assertThat(edmsDownloadList).hasSize(databaseSizeBeforeUpdate);
        EdmsDownload testEdmsDownload = edmsDownloadList.get(edmsDownloadList.size() - 1);
        assertThat(testEdmsDownload.getActualDirectory()).isEqualTo(UPDATED_ACTUAL_DIRECTORY);
        assertThat(testEdmsDownload.getActualFilename()).isEqualTo(UPDATED_ACTUAL_FILENAME);

        // Validate the EdmsDownload in ElasticSearch
        /*EdmsDownload edmsDownloadEs = edmsDownloadSearchRepository.findOne(testEdmsDownload.getId());
        assertThat(edmsDownloadEs).isEqualToComparingFieldByField(testEdmsDownload);*/
    }

    @Test
    @Transactional
    public void updateNonExistingEdmsDownload() throws Exception {
        int databaseSizeBeforeUpdate = edmsDownloadRepository.findAll().size();

        // Create the EdmsDownload
        EdmsDownloadDTO edmsDownloadDTO = edmsDownloadMapper.edmsDownloadToEdmsDownloadDTO(edmsDownload);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEdmsDownloadMockMvc.perform(put("/api/edms-downloads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsDownloadDTO)))
            .andExpect(status().isCreated());

        // Validate the EdmsDownload in the database
        List<EdmsDownload> edmsDownloadList = edmsDownloadRepository.findAll();
        assertThat(edmsDownloadList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEdmsDownload() throws Exception {
        // Initialize the database
        edmsDownloadRepository.saveAndFlush(edmsDownload);
        edmsDownloadSearchRepository.save(edmsDownload);
        int databaseSizeBeforeDelete = edmsDownloadRepository.findAll().size();

        // Get the edmsDownload
        restEdmsDownloadMockMvc.perform(delete("/api/edms-downloads/{id}", edmsDownload.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean edmsDownloadExistsInEs = edmsDownloadSearchRepository.exists(edmsDownload.getId());
        assertThat(edmsDownloadExistsInEs).isFalse();

        // Validate the database is empty
        List<EdmsDownload> edmsDownloadList = edmsDownloadRepository.findAll();
        assertThat(edmsDownloadList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEdmsDownload() throws Exception {
        // Initialize the database
        edmsDownloadRepository.saveAndFlush(edmsDownload);
        edmsDownloadSearchRepository.save(edmsDownload);

        // Search the edmsDownload
        restEdmsDownloadMockMvc.perform(get("/api/_search/edms-downloads?query=id:" + edmsDownload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edmsDownload.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualDirectory").value(hasItem(DEFAULT_ACTUAL_DIRECTORY.toString())))
            .andExpect(jsonPath("$.[*].actualFilename").value(hasItem(DEFAULT_ACTUAL_FILENAME.toString())));
    }
}
