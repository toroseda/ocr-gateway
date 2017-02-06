package ae.etisalat.eim.ocr.gateway.web.rest;

import ae.etisalat.eim.ocr.gateway.OcrGatewayApp;

import ae.etisalat.eim.ocr.gateway.domain.EdmsRequest;
import ae.etisalat.eim.ocr.gateway.repository.EdmsRequestRepository;
import ae.etisalat.eim.ocr.gateway.service.EdmsRequestService;
import ae.etisalat.eim.ocr.gateway.repository.search.EdmsRequestSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsRequestDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.EdmsRequestMapper;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EdmsRequestResource REST controller.
 *
 * @see EdmsRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OcrGatewayApp.class)
public class EdmsRequestResourceIntTest {

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_REQUEST_ID = "AAAAAAAAAA";
    private static final String UPDATED_SUB_REQUEST_ID = "BBBBBBBBBB";

    private static final String DEFAULT_AREA_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AREA_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LAST_RUN_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_RUN_BY = "BBBBBBBBBB";

    private static final Integer DEFAULT_LAST_RUN_DUR = 1;
    private static final Integer UPDATED_LAST_RUN_DUR = 2;

    private static final LocalDate DEFAULT_LAST_RUN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_RUN_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private EdmsRequestRepository edmsRequestRepository;

    @Inject
    private EdmsRequestMapper edmsRequestMapper;

    @Inject
    private EdmsRequestService edmsRequestService;

    @Inject
    private EdmsRequestSearchRepository edmsRequestSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEdmsRequestMockMvc;

    private EdmsRequest edmsRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EdmsRequestResource edmsRequestResource = new EdmsRequestResource();
        ReflectionTestUtils.setField(edmsRequestResource, "edmsRequestService", edmsRequestService);
        this.restEdmsRequestMockMvc = MockMvcBuilders.standaloneSetup(edmsRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EdmsRequest createEntity(EntityManager em) {
        EdmsRequest edmsRequest = new EdmsRequest()
                .accountNumber(DEFAULT_ACCOUNT_NUMBER)
                .subRequestId(DEFAULT_SUB_REQUEST_ID)
                .areaCode(DEFAULT_AREA_CODE)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE)
                .lastRunBy(DEFAULT_LAST_RUN_BY)
                .lastRunDur(DEFAULT_LAST_RUN_DUR)
                .lastRunDate(DEFAULT_LAST_RUN_DATE);
        return edmsRequest;
    }

    @Before
    public void initTest() {
        edmsRequestSearchRepository.deleteAll();
        edmsRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createEdmsRequest() throws Exception {
        int databaseSizeBeforeCreate = edmsRequestRepository.findAll().size();

        // Create the EdmsRequest
        EdmsRequestDTO edmsRequestDTO = edmsRequestMapper.edmsRequestToEdmsRequestDTO(edmsRequest);

        restEdmsRequestMockMvc.perform(post("/api/edms-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the EdmsRequest in the database
        List<EdmsRequest> edmsRequestList = edmsRequestRepository.findAll();
        assertThat(edmsRequestList).hasSize(databaseSizeBeforeCreate + 1);
        EdmsRequest testEdmsRequest = edmsRequestList.get(edmsRequestList.size() - 1);
        assertThat(testEdmsRequest.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testEdmsRequest.getSubRequestId()).isEqualTo(DEFAULT_SUB_REQUEST_ID);
        assertThat(testEdmsRequest.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testEdmsRequest.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEdmsRequest.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEdmsRequest.getLastRunBy()).isEqualTo(DEFAULT_LAST_RUN_BY);
        assertThat(testEdmsRequest.getLastRunDur()).isEqualTo(DEFAULT_LAST_RUN_DUR);
        assertThat(testEdmsRequest.getLastRunDate()).isEqualTo(DEFAULT_LAST_RUN_DATE);

        // Validate the EdmsRequest in ElasticSearch
        EdmsRequest edmsRequestEs = edmsRequestSearchRepository.findOne(testEdmsRequest.getId());
        assertThat(edmsRequestEs).isEqualToComparingFieldByField(testEdmsRequest);
    }

    @Test
    @Transactional
    public void createEdmsRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = edmsRequestRepository.findAll().size();

        // Create the EdmsRequest with an existing ID
        EdmsRequest existingEdmsRequest = new EdmsRequest();
        existingEdmsRequest.setId(1L);
        EdmsRequestDTO existingEdmsRequestDTO = edmsRequestMapper.edmsRequestToEdmsRequestDTO(existingEdmsRequest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEdmsRequestMockMvc.perform(post("/api/edms-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEdmsRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EdmsRequest> edmsRequestList = edmsRequestRepository.findAll();
        assertThat(edmsRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAccountNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = edmsRequestRepository.findAll().size();
        // set the field null
        edmsRequest.setAccountNumber(null);

        // Create the EdmsRequest, which fails.
        EdmsRequestDTO edmsRequestDTO = edmsRequestMapper.edmsRequestToEdmsRequestDTO(edmsRequest);

        restEdmsRequestMockMvc.perform(post("/api/edms-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsRequestDTO)))
            .andExpect(status().isBadRequest());

        List<EdmsRequest> edmsRequestList = edmsRequestRepository.findAll();
        assertThat(edmsRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = edmsRequestRepository.findAll().size();
        // set the field null
        edmsRequest.setSubRequestId(null);

        // Create the EdmsRequest, which fails.
        EdmsRequestDTO edmsRequestDTO = edmsRequestMapper.edmsRequestToEdmsRequestDTO(edmsRequest);

        restEdmsRequestMockMvc.perform(post("/api/edms-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsRequestDTO)))
            .andExpect(status().isBadRequest());

        List<EdmsRequest> edmsRequestList = edmsRequestRepository.findAll();
        assertThat(edmsRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = edmsRequestRepository.findAll().size();
        // set the field null
        edmsRequest.setAreaCode(null);

        // Create the EdmsRequest, which fails.
        EdmsRequestDTO edmsRequestDTO = edmsRequestMapper.edmsRequestToEdmsRequestDTO(edmsRequest);

        restEdmsRequestMockMvc.perform(post("/api/edms-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsRequestDTO)))
            .andExpect(status().isBadRequest());

        List<EdmsRequest> edmsRequestList = edmsRequestRepository.findAll();
        assertThat(edmsRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEdmsRequests() throws Exception {
        // Initialize the database
        edmsRequestRepository.saveAndFlush(edmsRequest);

        // Get all the edmsRequestList
        restEdmsRequestMockMvc.perform(get("/api/edms-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edmsRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].subRequestId").value(hasItem(DEFAULT_SUB_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastRunBy").value(hasItem(DEFAULT_LAST_RUN_BY.toString())))
            .andExpect(jsonPath("$.[*].lastRunDur").value(hasItem(DEFAULT_LAST_RUN_DUR)))
            .andExpect(jsonPath("$.[*].lastRunDate").value(hasItem(DEFAULT_LAST_RUN_DATE.toString())));
    }

    @Test
    @Transactional
    public void getEdmsRequest() throws Exception {
        // Initialize the database
        edmsRequestRepository.saveAndFlush(edmsRequest);

        // Get the edmsRequest
        restEdmsRequestMockMvc.perform(get("/api/edms-requests/{id}", edmsRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(edmsRequest.getId().intValue()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER.toString()))
            .andExpect(jsonPath("$.subRequestId").value(DEFAULT_SUB_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.lastRunBy").value(DEFAULT_LAST_RUN_BY.toString()))
            .andExpect(jsonPath("$.lastRunDur").value(DEFAULT_LAST_RUN_DUR))
            .andExpect(jsonPath("$.lastRunDate").value(DEFAULT_LAST_RUN_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEdmsRequest() throws Exception {
        // Get the edmsRequest
        restEdmsRequestMockMvc.perform(get("/api/edms-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEdmsRequest() throws Exception {
        // Initialize the database
        edmsRequestRepository.saveAndFlush(edmsRequest);
        edmsRequestSearchRepository.save(edmsRequest);
        int databaseSizeBeforeUpdate = edmsRequestRepository.findAll().size();

        // Update the edmsRequest
        EdmsRequest updatedEdmsRequest = edmsRequestRepository.findOne(edmsRequest.getId());
        updatedEdmsRequest
                .accountNumber(UPDATED_ACCOUNT_NUMBER)
                .subRequestId(UPDATED_SUB_REQUEST_ID)
                .areaCode(UPDATED_AREA_CODE)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE)
                .lastRunBy(UPDATED_LAST_RUN_BY)
                .lastRunDur(UPDATED_LAST_RUN_DUR)
                .lastRunDate(UPDATED_LAST_RUN_DATE);
        EdmsRequestDTO edmsRequestDTO = edmsRequestMapper.edmsRequestToEdmsRequestDTO(updatedEdmsRequest);

        restEdmsRequestMockMvc.perform(put("/api/edms-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsRequestDTO)))
            .andExpect(status().isOk());

        // Validate the EdmsRequest in the database
        List<EdmsRequest> edmsRequestList = edmsRequestRepository.findAll();
        assertThat(edmsRequestList).hasSize(databaseSizeBeforeUpdate);
        EdmsRequest testEdmsRequest = edmsRequestList.get(edmsRequestList.size() - 1);
        assertThat(testEdmsRequest.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testEdmsRequest.getSubRequestId()).isEqualTo(UPDATED_SUB_REQUEST_ID);
        assertThat(testEdmsRequest.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testEdmsRequest.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEdmsRequest.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEdmsRequest.getLastRunBy()).isEqualTo(UPDATED_LAST_RUN_BY);
        assertThat(testEdmsRequest.getLastRunDur()).isEqualTo(UPDATED_LAST_RUN_DUR);
        assertThat(testEdmsRequest.getLastRunDate()).isEqualTo(UPDATED_LAST_RUN_DATE);

        // Validate the EdmsRequest in ElasticSearch
        EdmsRequest edmsRequestEs = edmsRequestSearchRepository.findOne(testEdmsRequest.getId());
        assertThat(edmsRequestEs).isEqualToComparingFieldByField(testEdmsRequest);
    }

    @Test
    @Transactional
    public void updateNonExistingEdmsRequest() throws Exception {
        int databaseSizeBeforeUpdate = edmsRequestRepository.findAll().size();

        // Create the EdmsRequest
        EdmsRequestDTO edmsRequestDTO = edmsRequestMapper.edmsRequestToEdmsRequestDTO(edmsRequest);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEdmsRequestMockMvc.perform(put("/api/edms-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the EdmsRequest in the database
        List<EdmsRequest> edmsRequestList = edmsRequestRepository.findAll();
        assertThat(edmsRequestList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEdmsRequest() throws Exception {
        // Initialize the database
        edmsRequestRepository.saveAndFlush(edmsRequest);
        edmsRequestSearchRepository.save(edmsRequest);
        int databaseSizeBeforeDelete = edmsRequestRepository.findAll().size();

        // Get the edmsRequest
        restEdmsRequestMockMvc.perform(delete("/api/edms-requests/{id}", edmsRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean edmsRequestExistsInEs = edmsRequestSearchRepository.exists(edmsRequest.getId());
        assertThat(edmsRequestExistsInEs).isFalse();

        // Validate the database is empty
        List<EdmsRequest> edmsRequestList = edmsRequestRepository.findAll();
        assertThat(edmsRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEdmsRequest() throws Exception {
        // Initialize the database
        edmsRequestRepository.saveAndFlush(edmsRequest);
        edmsRequestSearchRepository.save(edmsRequest);

        // Search the edmsRequest
        restEdmsRequestMockMvc.perform(get("/api/_search/edms-requests?query=id:" + edmsRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edmsRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].subRequestId").value(hasItem(DEFAULT_SUB_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastRunBy").value(hasItem(DEFAULT_LAST_RUN_BY.toString())))
            .andExpect(jsonPath("$.[*].lastRunDur").value(hasItem(DEFAULT_LAST_RUN_DUR)))
            .andExpect(jsonPath("$.[*].lastRunDate").value(hasItem(DEFAULT_LAST_RUN_DATE.toString())));
    }
}
