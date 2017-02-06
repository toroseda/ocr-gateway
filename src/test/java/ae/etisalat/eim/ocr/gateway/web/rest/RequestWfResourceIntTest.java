package ae.etisalat.eim.ocr.gateway.web.rest;

import ae.etisalat.eim.ocr.gateway.OcrGatewayApp;

import ae.etisalat.eim.ocr.gateway.domain.RequestWf;
import ae.etisalat.eim.ocr.gateway.repository.RequestWfRepository;
import ae.etisalat.eim.ocr.gateway.service.RequestWfService;
import ae.etisalat.eim.ocr.gateway.repository.search.RequestWfSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.RequestWfDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.RequestWfMapper;

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
 * Test class for the RequestWfResource REST controller.
 *
 * @see RequestWfResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OcrGatewayApp.class)
public class RequestWfResourceIntTest {

    private static final Integer DEFAULT_STATUS_ID = 1;
    private static final Integer UPDATED_STATUS_ID = 2;

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Inject
    private RequestWfRepository requestWfRepository;

    @Inject
    private RequestWfMapper requestWfMapper;

    @Inject
    private RequestWfService requestWfService;

    @Inject
    private RequestWfSearchRepository requestWfSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRequestWfMockMvc;

    private RequestWf requestWf;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequestWfResource requestWfResource = new RequestWfResource();
        ReflectionTestUtils.setField(requestWfResource, "requestWfService", requestWfService);
        this.restRequestWfMockMvc = MockMvcBuilders.standaloneSetup(requestWfResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestWf createEntity(EntityManager em) {
        RequestWf requestWf = new RequestWf()
                .statusId(DEFAULT_STATUS_ID)
                .updatedBy(DEFAULT_UPDATED_BY);
        return requestWf;
    }

    @Before
    public void initTest() {
        requestWfSearchRepository.deleteAll();
        requestWf = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequestWf() throws Exception {
        int databaseSizeBeforeCreate = requestWfRepository.findAll().size();

        // Create the RequestWf
        RequestWfDTO requestWfDTO = requestWfMapper.requestWfToRequestWfDTO(requestWf);

        restRequestWfMockMvc.perform(post("/api/request-wfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestWfDTO)))
            .andExpect(status().isCreated());

        // Validate the RequestWf in the database
        List<RequestWf> requestWfList = requestWfRepository.findAll();
        assertThat(requestWfList).hasSize(databaseSizeBeforeCreate + 1);
        RequestWf testRequestWf = requestWfList.get(requestWfList.size() - 1);
        assertThat(testRequestWf.getStatusId()).isEqualTo(DEFAULT_STATUS_ID);
        assertThat(testRequestWf.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

        // Validate the RequestWf in ElasticSearch
        RequestWf requestWfEs = requestWfSearchRepository.findOne(testRequestWf.getId());
        assertThat(requestWfEs).isEqualToComparingFieldByField(testRequestWf);
    }

    @Test
    @Transactional
    public void createRequestWfWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requestWfRepository.findAll().size();

        // Create the RequestWf with an existing ID
        RequestWf existingRequestWf = new RequestWf();
        existingRequestWf.setId(1L);
        RequestWfDTO existingRequestWfDTO = requestWfMapper.requestWfToRequestWfDTO(existingRequestWf);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestWfMockMvc.perform(post("/api/request-wfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingRequestWfDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RequestWf> requestWfList = requestWfRepository.findAll();
        assertThat(requestWfList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestWfRepository.findAll().size();
        // set the field null
        requestWf.setStatusId(null);

        // Create the RequestWf, which fails.
        RequestWfDTO requestWfDTO = requestWfMapper.requestWfToRequestWfDTO(requestWf);

        restRequestWfMockMvc.perform(post("/api/request-wfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestWfDTO)))
            .andExpect(status().isBadRequest());

        List<RequestWf> requestWfList = requestWfRepository.findAll();
        assertThat(requestWfList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequestWfs() throws Exception {
        // Initialize the database
        requestWfRepository.saveAndFlush(requestWf);

        // Get all the requestWfList
        restRequestWfMockMvc.perform(get("/api/request-wfs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestWf.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusId").value(hasItem(DEFAULT_STATUS_ID)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getRequestWf() throws Exception {
        // Initialize the database
        requestWfRepository.saveAndFlush(requestWf);

        // Get the requestWf
        restRequestWfMockMvc.perform(get("/api/request-wfs/{id}", requestWf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requestWf.getId().intValue()))
            .andExpect(jsonPath("$.statusId").value(DEFAULT_STATUS_ID))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequestWf() throws Exception {
        // Get the requestWf
        restRequestWfMockMvc.perform(get("/api/request-wfs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequestWf() throws Exception {
        // Initialize the database
        requestWfRepository.saveAndFlush(requestWf);
        requestWfSearchRepository.save(requestWf);
        int databaseSizeBeforeUpdate = requestWfRepository.findAll().size();

        // Update the requestWf
        RequestWf updatedRequestWf = requestWfRepository.findOne(requestWf.getId());
        updatedRequestWf
                .statusId(UPDATED_STATUS_ID)
                .updatedBy(UPDATED_UPDATED_BY);
        RequestWfDTO requestWfDTO = requestWfMapper.requestWfToRequestWfDTO(updatedRequestWf);

        restRequestWfMockMvc.perform(put("/api/request-wfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestWfDTO)))
            .andExpect(status().isOk());

        // Validate the RequestWf in the database
        List<RequestWf> requestWfList = requestWfRepository.findAll();
        assertThat(requestWfList).hasSize(databaseSizeBeforeUpdate);
        RequestWf testRequestWf = requestWfList.get(requestWfList.size() - 1);
        assertThat(testRequestWf.getStatusId()).isEqualTo(UPDATED_STATUS_ID);
        assertThat(testRequestWf.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

        // Validate the RequestWf in ElasticSearch
        RequestWf requestWfEs = requestWfSearchRepository.findOne(testRequestWf.getId());
        assertThat(requestWfEs).isEqualToComparingFieldByField(testRequestWf);
    }

    @Test
    @Transactional
    public void updateNonExistingRequestWf() throws Exception {
        int databaseSizeBeforeUpdate = requestWfRepository.findAll().size();

        // Create the RequestWf
        RequestWfDTO requestWfDTO = requestWfMapper.requestWfToRequestWfDTO(requestWf);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRequestWfMockMvc.perform(put("/api/request-wfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestWfDTO)))
            .andExpect(status().isCreated());

        // Validate the RequestWf in the database
        List<RequestWf> requestWfList = requestWfRepository.findAll();
        assertThat(requestWfList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRequestWf() throws Exception {
        // Initialize the database
        requestWfRepository.saveAndFlush(requestWf);
        requestWfSearchRepository.save(requestWf);
        int databaseSizeBeforeDelete = requestWfRepository.findAll().size();

        // Get the requestWf
        restRequestWfMockMvc.perform(delete("/api/request-wfs/{id}", requestWf.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean requestWfExistsInEs = requestWfSearchRepository.exists(requestWf.getId());
        assertThat(requestWfExistsInEs).isFalse();

        // Validate the database is empty
        List<RequestWf> requestWfList = requestWfRepository.findAll();
        assertThat(requestWfList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRequestWf() throws Exception {
        // Initialize the database
        requestWfRepository.saveAndFlush(requestWf);
        requestWfSearchRepository.save(requestWf);

        // Search the requestWf
        restRequestWfMockMvc.perform(get("/api/_search/request-wfs?query=id:" + requestWf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestWf.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusId").value(hasItem(DEFAULT_STATUS_ID)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }
}
