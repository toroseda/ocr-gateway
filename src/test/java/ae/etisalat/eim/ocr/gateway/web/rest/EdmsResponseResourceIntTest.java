package ae.etisalat.eim.ocr.gateway.web.rest;

import ae.etisalat.eim.ocr.gateway.OcrGatewayApp;

import ae.etisalat.eim.ocr.gateway.domain.EdmsResponse;
import ae.etisalat.eim.ocr.gateway.repository.EdmsResponseRepository;
import ae.etisalat.eim.ocr.gateway.service.EdmsResponseService;
import ae.etisalat.eim.ocr.gateway.repository.search.EdmsResponseSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsResponseDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.EdmsResponseMapper;

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
 * Test class for the EdmsResponseResource REST controller.
 *
 * @see EdmsResponseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OcrGatewayApp.class)
public class EdmsResponseResourceIntTest {

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_REQUEST_ID = "AAAAAAAAAA";
    private static final String UPDATED_SUB_REQUEST_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_DOC_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_DOC_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PROMISED_DIRECTORY = "AAAAAAAAAA";
    private static final String UPDATED_PROMISED_DIRECTORY = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_DOC_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_DOC_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_SUB_REQUEST_ID = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_SUB_REQUEST_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_AEA_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_AEA_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_RESPONSE_DOC_COUNT = 1;
    private static final Integer UPDATED_RESPONSE_DOC_COUNT = 2;

    private static final Integer DEFAULT_DIRECTORY_AVILABLE_FLG = 1;
    private static final Integer UPDATED_DIRECTORY_AVILABLE_FLG = 2;

    private static final Integer DEFAULT_FILE_COUNT = 1;
    private static final Integer UPDATED_FILE_COUNT = 2;

    @Inject
    private EdmsResponseRepository edmsResponseRepository;

    @Inject
    private EdmsResponseMapper edmsResponseMapper;

    @Inject
    private EdmsResponseService edmsResponseService;

    @Inject
    private EdmsResponseSearchRepository edmsResponseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEdmsResponseMockMvc;

    private EdmsResponse edmsResponse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EdmsResponseResource edmsResponseResource = new EdmsResponseResource();
        ReflectionTestUtils.setField(edmsResponseResource, "edmsResponseService", edmsResponseService);
        this.restEdmsResponseMockMvc = MockMvcBuilders.standaloneSetup(edmsResponseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EdmsResponse createEntity(EntityManager em) {
        EdmsResponse edmsResponse = new EdmsResponse()
                .accountNumber(DEFAULT_ACCOUNT_NUMBER)
                .subRequestId(DEFAULT_SUB_REQUEST_ID)
                .requestDocType(DEFAULT_REQUEST_DOC_TYPE)
                .errorCode(DEFAULT_ERROR_CODE)
                .errorDescription(DEFAULT_ERROR_DESCRIPTION)
                .promisedDirectory(DEFAULT_PROMISED_DIRECTORY)
                .responseDocType(DEFAULT_RESPONSE_DOC_TYPE)
                .responseSubRequestId(DEFAULT_RESPONSE_SUB_REQUEST_ID)
                .responseAeaCode(DEFAULT_RESPONSE_AEA_CODE)
                .responseDocCount(DEFAULT_RESPONSE_DOC_COUNT)
                .directoryAvilableFlg(DEFAULT_DIRECTORY_AVILABLE_FLG)
                .fileCount(DEFAULT_FILE_COUNT);
        return edmsResponse;
    }

    @Before
    public void initTest() {
        edmsResponseSearchRepository.deleteAll();
        edmsResponse = createEntity(em);
    }

    @Test
    @Transactional
    public void createEdmsResponse() throws Exception {
        int databaseSizeBeforeCreate = edmsResponseRepository.findAll().size();

        // Create the EdmsResponse
        EdmsResponseDTO edmsResponseDTO = edmsResponseMapper.edmsResponseToEdmsResponseDTO(edmsResponse);

        restEdmsResponseMockMvc.perform(post("/api/edms-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsResponseDTO)))
            .andExpect(status().isCreated());

        // Validate the EdmsResponse in the database
        List<EdmsResponse> edmsResponseList = edmsResponseRepository.findAll();
        assertThat(edmsResponseList).hasSize(databaseSizeBeforeCreate + 1);
        EdmsResponse testEdmsResponse = edmsResponseList.get(edmsResponseList.size() - 1);
        assertThat(testEdmsResponse.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testEdmsResponse.getSubRequestId()).isEqualTo(DEFAULT_SUB_REQUEST_ID);
        assertThat(testEdmsResponse.getRequestDocType()).isEqualTo(DEFAULT_REQUEST_DOC_TYPE);
        assertThat(testEdmsResponse.getErrorCode()).isEqualTo(DEFAULT_ERROR_CODE);
        assertThat(testEdmsResponse.getErrorDescription()).isEqualTo(DEFAULT_ERROR_DESCRIPTION);
        assertThat(testEdmsResponse.getPromisedDirectory()).isEqualTo(DEFAULT_PROMISED_DIRECTORY);
        assertThat(testEdmsResponse.getResponseDocType()).isEqualTo(DEFAULT_RESPONSE_DOC_TYPE);
        assertThat(testEdmsResponse.getResponseSubRequestId()).isEqualTo(DEFAULT_RESPONSE_SUB_REQUEST_ID);
        assertThat(testEdmsResponse.getResponseAeaCode()).isEqualTo(DEFAULT_RESPONSE_AEA_CODE);
        assertThat(testEdmsResponse.getResponseDocCount()).isEqualTo(DEFAULT_RESPONSE_DOC_COUNT);
        assertThat(testEdmsResponse.getDirectoryAvilableFlg()).isEqualTo(DEFAULT_DIRECTORY_AVILABLE_FLG);
        assertThat(testEdmsResponse.getFileCount()).isEqualTo(DEFAULT_FILE_COUNT);

        // Validate the EdmsResponse in ElasticSearch
        /*EdmsResponse edmsResponseEs = edmsResponseSearchRepository.findOne(testEdmsResponse.getId());
        assertThat(edmsResponseEs).isEqualToComparingFieldByField(testEdmsResponse);*/
    }

    @Test
    @Transactional
    public void createEdmsResponseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = edmsResponseRepository.findAll().size();

        // Create the EdmsResponse with an existing ID
        EdmsResponse existingEdmsResponse = new EdmsResponse();
        existingEdmsResponse.setId(1L);
        EdmsResponseDTO existingEdmsResponseDTO = edmsResponseMapper.edmsResponseToEdmsResponseDTO(existingEdmsResponse);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEdmsResponseMockMvc.perform(post("/api/edms-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEdmsResponseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EdmsResponse> edmsResponseList = edmsResponseRepository.findAll();
        assertThat(edmsResponseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAccountNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = edmsResponseRepository.findAll().size();
        // set the field null
        edmsResponse.setAccountNumber(null);

        // Create the EdmsResponse, which fails.
        EdmsResponseDTO edmsResponseDTO = edmsResponseMapper.edmsResponseToEdmsResponseDTO(edmsResponse);

        restEdmsResponseMockMvc.perform(post("/api/edms-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsResponseDTO)))
            .andExpect(status().isBadRequest());

        List<EdmsResponse> edmsResponseList = edmsResponseRepository.findAll();
        assertThat(edmsResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = edmsResponseRepository.findAll().size();
        // set the field null
        edmsResponse.setSubRequestId(null);

        // Create the EdmsResponse, which fails.
        EdmsResponseDTO edmsResponseDTO = edmsResponseMapper.edmsResponseToEdmsResponseDTO(edmsResponse);

        restEdmsResponseMockMvc.perform(post("/api/edms-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsResponseDTO)))
            .andExpect(status().isBadRequest());

        List<EdmsResponse> edmsResponseList = edmsResponseRepository.findAll();
        assertThat(edmsResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEdmsResponses() throws Exception {
        // Initialize the database
        edmsResponseRepository.saveAndFlush(edmsResponse);

        // Get all the edmsResponseList
        restEdmsResponseMockMvc.perform(get("/api/edms-responses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edmsResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].subRequestId").value(hasItem(DEFAULT_SUB_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].requestDocType").value(hasItem(DEFAULT_REQUEST_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].errorCode").value(hasItem(DEFAULT_ERROR_CODE.toString())))
            .andExpect(jsonPath("$.[*].errorDescription").value(hasItem(DEFAULT_ERROR_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].promisedDirectory").value(hasItem(DEFAULT_PROMISED_DIRECTORY.toString())))
            .andExpect(jsonPath("$.[*].responseDocType").value(hasItem(DEFAULT_RESPONSE_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].responseSubRequestId").value(hasItem(DEFAULT_RESPONSE_SUB_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].responseAeaCode").value(hasItem(DEFAULT_RESPONSE_AEA_CODE.toString())))
            .andExpect(jsonPath("$.[*].responseDocCount").value(hasItem(DEFAULT_RESPONSE_DOC_COUNT)))
            .andExpect(jsonPath("$.[*].directoryAvilableFlg").value(hasItem(DEFAULT_DIRECTORY_AVILABLE_FLG)))
            .andExpect(jsonPath("$.[*].fileCount").value(hasItem(DEFAULT_FILE_COUNT)));
    }

    @Test
    @Transactional
    public void getEdmsResponse() throws Exception {
        // Initialize the database
        edmsResponseRepository.saveAndFlush(edmsResponse);

        // Get the edmsResponse
        restEdmsResponseMockMvc.perform(get("/api/edms-responses/{id}", edmsResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(edmsResponse.getId().intValue()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER.toString()))
            .andExpect(jsonPath("$.subRequestId").value(DEFAULT_SUB_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.requestDocType").value(DEFAULT_REQUEST_DOC_TYPE.toString()))
            .andExpect(jsonPath("$.errorCode").value(DEFAULT_ERROR_CODE.toString()))
            .andExpect(jsonPath("$.errorDescription").value(DEFAULT_ERROR_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.promisedDirectory").value(DEFAULT_PROMISED_DIRECTORY.toString()))
            .andExpect(jsonPath("$.responseDocType").value(DEFAULT_RESPONSE_DOC_TYPE.toString()))
            .andExpect(jsonPath("$.responseSubRequestId").value(DEFAULT_RESPONSE_SUB_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.responseAeaCode").value(DEFAULT_RESPONSE_AEA_CODE.toString()))
            .andExpect(jsonPath("$.responseDocCount").value(DEFAULT_RESPONSE_DOC_COUNT))
            .andExpect(jsonPath("$.directoryAvilableFlg").value(DEFAULT_DIRECTORY_AVILABLE_FLG))
            .andExpect(jsonPath("$.fileCount").value(DEFAULT_FILE_COUNT));
    }

    @Test
    @Transactional
    public void getNonExistingEdmsResponse() throws Exception {
        // Get the edmsResponse
        restEdmsResponseMockMvc.perform(get("/api/edms-responses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEdmsResponse() throws Exception {
        // Initialize the database
        edmsResponseRepository.saveAndFlush(edmsResponse);
        edmsResponseSearchRepository.save(edmsResponse);
        int databaseSizeBeforeUpdate = edmsResponseRepository.findAll().size();

        // Update the edmsResponse
        EdmsResponse updatedEdmsResponse = edmsResponseRepository.findOne(edmsResponse.getId());
        updatedEdmsResponse
                .accountNumber(UPDATED_ACCOUNT_NUMBER)
                .subRequestId(UPDATED_SUB_REQUEST_ID)
                .requestDocType(UPDATED_REQUEST_DOC_TYPE)
                .errorCode(UPDATED_ERROR_CODE)
                .errorDescription(UPDATED_ERROR_DESCRIPTION)
                .promisedDirectory(UPDATED_PROMISED_DIRECTORY)
                .responseDocType(UPDATED_RESPONSE_DOC_TYPE)
                .responseSubRequestId(UPDATED_RESPONSE_SUB_REQUEST_ID)
                .responseAeaCode(UPDATED_RESPONSE_AEA_CODE)
                .responseDocCount(UPDATED_RESPONSE_DOC_COUNT)
                .directoryAvilableFlg(UPDATED_DIRECTORY_AVILABLE_FLG)
                .fileCount(UPDATED_FILE_COUNT);
        EdmsResponseDTO edmsResponseDTO = edmsResponseMapper.edmsResponseToEdmsResponseDTO(updatedEdmsResponse);

        restEdmsResponseMockMvc.perform(put("/api/edms-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsResponseDTO)))
            .andExpect(status().isOk());

        // Validate the EdmsResponse in the database
        List<EdmsResponse> edmsResponseList = edmsResponseRepository.findAll();
        assertThat(edmsResponseList).hasSize(databaseSizeBeforeUpdate);
        EdmsResponse testEdmsResponse = edmsResponseList.get(edmsResponseList.size() - 1);
        assertThat(testEdmsResponse.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testEdmsResponse.getSubRequestId()).isEqualTo(UPDATED_SUB_REQUEST_ID);
        assertThat(testEdmsResponse.getRequestDocType()).isEqualTo(UPDATED_REQUEST_DOC_TYPE);
        assertThat(testEdmsResponse.getErrorCode()).isEqualTo(UPDATED_ERROR_CODE);
        assertThat(testEdmsResponse.getErrorDescription()).isEqualTo(UPDATED_ERROR_DESCRIPTION);
        assertThat(testEdmsResponse.getPromisedDirectory()).isEqualTo(UPDATED_PROMISED_DIRECTORY);
        assertThat(testEdmsResponse.getResponseDocType()).isEqualTo(UPDATED_RESPONSE_DOC_TYPE);
        assertThat(testEdmsResponse.getResponseSubRequestId()).isEqualTo(UPDATED_RESPONSE_SUB_REQUEST_ID);
        assertThat(testEdmsResponse.getResponseAeaCode()).isEqualTo(UPDATED_RESPONSE_AEA_CODE);
        assertThat(testEdmsResponse.getResponseDocCount()).isEqualTo(UPDATED_RESPONSE_DOC_COUNT);
        assertThat(testEdmsResponse.getDirectoryAvilableFlg()).isEqualTo(UPDATED_DIRECTORY_AVILABLE_FLG);
        assertThat(testEdmsResponse.getFileCount()).isEqualTo(UPDATED_FILE_COUNT);

        // Validate the EdmsResponse in ElasticSearch
       /* EdmsResponse edmsResponseEs = edmsResponseSearchRepository.findOne(testEdmsResponse.getId());
        assertThat(edmsResponseEs).isEqualToComparingFieldByField(testEdmsResponse);*/
    }

    @Test
    @Transactional
    public void updateNonExistingEdmsResponse() throws Exception {
        int databaseSizeBeforeUpdate = edmsResponseRepository.findAll().size();

        // Create the EdmsResponse
        EdmsResponseDTO edmsResponseDTO = edmsResponseMapper.edmsResponseToEdmsResponseDTO(edmsResponse);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEdmsResponseMockMvc.perform(put("/api/edms-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(edmsResponseDTO)))
            .andExpect(status().isCreated());

        // Validate the EdmsResponse in the database
        List<EdmsResponse> edmsResponseList = edmsResponseRepository.findAll();
        assertThat(edmsResponseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEdmsResponse() throws Exception {
        // Initialize the database
        edmsResponseRepository.saveAndFlush(edmsResponse);
        edmsResponseSearchRepository.save(edmsResponse);
        int databaseSizeBeforeDelete = edmsResponseRepository.findAll().size();

        // Get the edmsResponse
        restEdmsResponseMockMvc.perform(delete("/api/edms-responses/{id}", edmsResponse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean edmsResponseExistsInEs = edmsResponseSearchRepository.exists(edmsResponse.getId());
        assertThat(edmsResponseExistsInEs).isFalse();

        // Validate the database is empty
        List<EdmsResponse> edmsResponseList = edmsResponseRepository.findAll();
        assertThat(edmsResponseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEdmsResponse() throws Exception {
        // Initialize the database
        edmsResponseRepository.saveAndFlush(edmsResponse);
        edmsResponseSearchRepository.save(edmsResponse);

        // Search the edmsResponse
        restEdmsResponseMockMvc.perform(get("/api/_search/edms-responses?query=id:" + edmsResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edmsResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].subRequestId").value(hasItem(DEFAULT_SUB_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].requestDocType").value(hasItem(DEFAULT_REQUEST_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].errorCode").value(hasItem(DEFAULT_ERROR_CODE.toString())))
            .andExpect(jsonPath("$.[*].errorDescription").value(hasItem(DEFAULT_ERROR_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].promisedDirectory").value(hasItem(DEFAULT_PROMISED_DIRECTORY.toString())))
            .andExpect(jsonPath("$.[*].responseDocType").value(hasItem(DEFAULT_RESPONSE_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].responseSubRequestId").value(hasItem(DEFAULT_RESPONSE_SUB_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].responseAeaCode").value(hasItem(DEFAULT_RESPONSE_AEA_CODE.toString())))
            .andExpect(jsonPath("$.[*].responseDocCount").value(hasItem(DEFAULT_RESPONSE_DOC_COUNT)))
            .andExpect(jsonPath("$.[*].directoryAvilableFlg").value(hasItem(DEFAULT_DIRECTORY_AVILABLE_FLG)))
            .andExpect(jsonPath("$.[*].fileCount").value(hasItem(DEFAULT_FILE_COUNT)));
    }
}
