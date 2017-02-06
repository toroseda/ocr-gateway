package ae.etisalat.eim.ocr.gateway.web.rest;

import ae.etisalat.eim.ocr.gateway.OcrGatewayApp;

import ae.etisalat.eim.ocr.gateway.domain.SessionWf;
import ae.etisalat.eim.ocr.gateway.repository.SessionWfRepository;
import ae.etisalat.eim.ocr.gateway.service.SessionWfService;
import ae.etisalat.eim.ocr.gateway.repository.search.SessionWfSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.SessionWfDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.SessionWfMapper;

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
 * Test class for the SessionWfResource REST controller.
 *
 * @see SessionWfResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OcrGatewayApp.class)
public class SessionWfResourceIntTest {

    private static final Integer DEFAULT_STATUS_ID = 1;
    private static final Integer UPDATED_STATUS_ID = 2;

    private static final Integer DEFAULT_WF_TYPE_ID = 1;
    private static final Integer UPDATED_WF_TYPE_ID = 2;

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Inject
    private SessionWfRepository sessionWfRepository;

    @Inject
    private SessionWfMapper sessionWfMapper;

    @Inject
    private SessionWfService sessionWfService;

    @Inject
    private SessionWfSearchRepository sessionWfSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSessionWfMockMvc;

    private SessionWf sessionWf;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SessionWfResource sessionWfResource = new SessionWfResource();
        ReflectionTestUtils.setField(sessionWfResource, "sessionWfService", sessionWfService);
        this.restSessionWfMockMvc = MockMvcBuilders.standaloneSetup(sessionWfResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionWf createEntity(EntityManager em) {
        SessionWf sessionWf = new SessionWf()
                .statusId(DEFAULT_STATUS_ID)
                .wfTypeId(DEFAULT_WF_TYPE_ID)
                .updatedBy(DEFAULT_UPDATED_BY);
        return sessionWf;
    }

    @Before
    public void initTest() {
        sessionWfSearchRepository.deleteAll();
        sessionWf = createEntity(em);
    }

    @Test
    @Transactional
    public void createSessionWf() throws Exception {
        int databaseSizeBeforeCreate = sessionWfRepository.findAll().size();

        // Create the SessionWf
        SessionWfDTO sessionWfDTO = sessionWfMapper.sessionWfToSessionWfDTO(sessionWf);

        restSessionWfMockMvc.perform(post("/api/session-wfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionWfDTO)))
            .andExpect(status().isCreated());

        // Validate the SessionWf in the database
        List<SessionWf> sessionWfList = sessionWfRepository.findAll();
        assertThat(sessionWfList).hasSize(databaseSizeBeforeCreate + 1);
        SessionWf testSessionWf = sessionWfList.get(sessionWfList.size() - 1);
        assertThat(testSessionWf.getStatusId()).isEqualTo(DEFAULT_STATUS_ID);
        assertThat(testSessionWf.getWfTypeId()).isEqualTo(DEFAULT_WF_TYPE_ID);
        assertThat(testSessionWf.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

        // Validate the SessionWf in ElasticSearch
        SessionWf sessionWfEs = sessionWfSearchRepository.findOne(testSessionWf.getId());
        assertThat(sessionWfEs).isEqualToComparingFieldByField(testSessionWf);
    }

    @Test
    @Transactional
    public void createSessionWfWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sessionWfRepository.findAll().size();

        // Create the SessionWf with an existing ID
        SessionWf existingSessionWf = new SessionWf();
        existingSessionWf.setId(1L);
        SessionWfDTO existingSessionWfDTO = sessionWfMapper.sessionWfToSessionWfDTO(existingSessionWf);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionWfMockMvc.perform(post("/api/session-wfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSessionWfDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SessionWf> sessionWfList = sessionWfRepository.findAll();
        assertThat(sessionWfList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionWfRepository.findAll().size();
        // set the field null
        sessionWf.setStatusId(null);

        // Create the SessionWf, which fails.
        SessionWfDTO sessionWfDTO = sessionWfMapper.sessionWfToSessionWfDTO(sessionWf);

        restSessionWfMockMvc.perform(post("/api/session-wfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionWfDTO)))
            .andExpect(status().isBadRequest());

        List<SessionWf> sessionWfList = sessionWfRepository.findAll();
        assertThat(sessionWfList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWfTypeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionWfRepository.findAll().size();
        // set the field null
        sessionWf.setWfTypeId(null);

        // Create the SessionWf, which fails.
        SessionWfDTO sessionWfDTO = sessionWfMapper.sessionWfToSessionWfDTO(sessionWf);

        restSessionWfMockMvc.perform(post("/api/session-wfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionWfDTO)))
            .andExpect(status().isBadRequest());

        List<SessionWf> sessionWfList = sessionWfRepository.findAll();
        assertThat(sessionWfList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSessionWfs() throws Exception {
        // Initialize the database
        sessionWfRepository.saveAndFlush(sessionWf);

        // Get all the sessionWfList
        restSessionWfMockMvc.perform(get("/api/session-wfs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionWf.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusId").value(hasItem(DEFAULT_STATUS_ID)))
            .andExpect(jsonPath("$.[*].wfTypeId").value(hasItem(DEFAULT_WF_TYPE_ID)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getSessionWf() throws Exception {
        // Initialize the database
        sessionWfRepository.saveAndFlush(sessionWf);

        // Get the sessionWf
        restSessionWfMockMvc.perform(get("/api/session-wfs/{id}", sessionWf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sessionWf.getId().intValue()))
            .andExpect(jsonPath("$.statusId").value(DEFAULT_STATUS_ID))
            .andExpect(jsonPath("$.wfTypeId").value(DEFAULT_WF_TYPE_ID))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSessionWf() throws Exception {
        // Get the sessionWf
        restSessionWfMockMvc.perform(get("/api/session-wfs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSessionWf() throws Exception {
        // Initialize the database
        sessionWfRepository.saveAndFlush(sessionWf);
        sessionWfSearchRepository.save(sessionWf);
        int databaseSizeBeforeUpdate = sessionWfRepository.findAll().size();

        // Update the sessionWf
        SessionWf updatedSessionWf = sessionWfRepository.findOne(sessionWf.getId());
        updatedSessionWf
                .statusId(UPDATED_STATUS_ID)
                .wfTypeId(UPDATED_WF_TYPE_ID)
                .updatedBy(UPDATED_UPDATED_BY);
        SessionWfDTO sessionWfDTO = sessionWfMapper.sessionWfToSessionWfDTO(updatedSessionWf);

        restSessionWfMockMvc.perform(put("/api/session-wfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionWfDTO)))
            .andExpect(status().isOk());

        // Validate the SessionWf in the database
        List<SessionWf> sessionWfList = sessionWfRepository.findAll();
        assertThat(sessionWfList).hasSize(databaseSizeBeforeUpdate);
        SessionWf testSessionWf = sessionWfList.get(sessionWfList.size() - 1);
        assertThat(testSessionWf.getStatusId()).isEqualTo(UPDATED_STATUS_ID);
        assertThat(testSessionWf.getWfTypeId()).isEqualTo(UPDATED_WF_TYPE_ID);
        assertThat(testSessionWf.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

        // Validate the SessionWf in ElasticSearch
        SessionWf sessionWfEs = sessionWfSearchRepository.findOne(testSessionWf.getId());
        assertThat(sessionWfEs).isEqualToComparingFieldByField(testSessionWf);
    }

    @Test
    @Transactional
    public void updateNonExistingSessionWf() throws Exception {
        int databaseSizeBeforeUpdate = sessionWfRepository.findAll().size();

        // Create the SessionWf
        SessionWfDTO sessionWfDTO = sessionWfMapper.sessionWfToSessionWfDTO(sessionWf);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSessionWfMockMvc.perform(put("/api/session-wfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionWfDTO)))
            .andExpect(status().isCreated());

        // Validate the SessionWf in the database
        List<SessionWf> sessionWfList = sessionWfRepository.findAll();
        assertThat(sessionWfList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSessionWf() throws Exception {
        // Initialize the database
        sessionWfRepository.saveAndFlush(sessionWf);
        sessionWfSearchRepository.save(sessionWf);
        int databaseSizeBeforeDelete = sessionWfRepository.findAll().size();

        // Get the sessionWf
        restSessionWfMockMvc.perform(delete("/api/session-wfs/{id}", sessionWf.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean sessionWfExistsInEs = sessionWfSearchRepository.exists(sessionWf.getId());
        assertThat(sessionWfExistsInEs).isFalse();

        // Validate the database is empty
        List<SessionWf> sessionWfList = sessionWfRepository.findAll();
        assertThat(sessionWfList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSessionWf() throws Exception {
        // Initialize the database
        sessionWfRepository.saveAndFlush(sessionWf);
        sessionWfSearchRepository.save(sessionWf);

        // Search the sessionWf
        restSessionWfMockMvc.perform(get("/api/_search/session-wfs?query=id:" + sessionWf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionWf.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusId").value(hasItem(DEFAULT_STATUS_ID)))
            .andExpect(jsonPath("$.[*].wfTypeId").value(hasItem(DEFAULT_WF_TYPE_ID)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }
}
