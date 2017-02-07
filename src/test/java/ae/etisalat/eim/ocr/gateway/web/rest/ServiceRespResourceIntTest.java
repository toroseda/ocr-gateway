package ae.etisalat.eim.ocr.gateway.web.rest;

import ae.etisalat.eim.ocr.gateway.OcrGatewayApp;

import ae.etisalat.eim.ocr.gateway.domain.ServiceResp;
import ae.etisalat.eim.ocr.gateway.repository.ServiceRespRepository;
import ae.etisalat.eim.ocr.gateway.service.ServiceRespService;
import ae.etisalat.eim.ocr.gateway.repository.search.ServiceRespSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.ServiceRespDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.ServiceRespMapper;

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
import org.springframework.util.Base64Utils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static ae.etisalat.eim.ocr.gateway.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ServiceRespResource REST controller.
 *
 * @see ServiceRespResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OcrGatewayApp.class)
public class ServiceRespResourceIntTest {

    private static final byte[] DEFAULT_RAW_JSON = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_RAW_JSON = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_RAW_JSON_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_RAW_JSON_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DOCUMENT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_IMAGE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LAST_RUN_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_RUN_BY = "BBBBBBBBBB";

    private static final Integer DEFAULT_LAST_RUN_DUR = 1;
    private static final Integer UPDATED_LAST_RUN_DUR = 2;

    private static final ZonedDateTime DEFAULT_LAST_RUN_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_RUN_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private ServiceRespRepository serviceRespRepository;

    @Inject
    private ServiceRespMapper serviceRespMapper;

    @Inject
    private ServiceRespService serviceRespService;

    @Inject
    private ServiceRespSearchRepository serviceRespSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restServiceRespMockMvc;

    private ServiceResp serviceResp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceRespResource serviceRespResource = new ServiceRespResource();
        ReflectionTestUtils.setField(serviceRespResource, "serviceRespService", serviceRespService);
        this.restServiceRespMockMvc = MockMvcBuilders.standaloneSetup(serviceRespResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceResp createEntity(EntityManager em) {
        ServiceResp serviceResp = new ServiceResp()
                .rawJson(DEFAULT_RAW_JSON)
                .rawJsonContentType(DEFAULT_RAW_JSON_CONTENT_TYPE)
                .documentImage(DEFAULT_DOCUMENT_IMAGE)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE)
                .lastRunBy(DEFAULT_LAST_RUN_BY)
                .lastRunDur(DEFAULT_LAST_RUN_DUR)
                .lastRunDate(DEFAULT_LAST_RUN_DATE);
        return serviceResp;
    }

    @Before
    public void initTest() {
        serviceRespSearchRepository.deleteAll();
        serviceResp = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceResp() throws Exception {
        int databaseSizeBeforeCreate = serviceRespRepository.findAll().size();

        // Create the ServiceResp
        ServiceRespDTO serviceRespDTO = serviceRespMapper.serviceRespToServiceRespDTO(serviceResp);

        restServiceRespMockMvc.perform(post("/api/service-resps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceRespDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceResp in the database
        List<ServiceResp> serviceRespList = serviceRespRepository.findAll();
        assertThat(serviceRespList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceResp testServiceResp = serviceRespList.get(serviceRespList.size() - 1);
        assertThat(testServiceResp.getRawJson()).isEqualTo(DEFAULT_RAW_JSON);
        assertThat(testServiceResp.getRawJsonContentType()).isEqualTo(DEFAULT_RAW_JSON_CONTENT_TYPE);
        assertThat(testServiceResp.getDocumentImage()).isEqualTo(DEFAULT_DOCUMENT_IMAGE);
        assertThat(testServiceResp.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testServiceResp.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testServiceResp.getLastRunBy()).isEqualTo(DEFAULT_LAST_RUN_BY);
        assertThat(testServiceResp.getLastRunDur()).isEqualTo(DEFAULT_LAST_RUN_DUR);
        assertThat(testServiceResp.getLastRunDate()).isEqualTo(DEFAULT_LAST_RUN_DATE);

        // Validate the ServiceResp in ElasticSearch
        /*ServiceResp serviceRespEs = serviceRespSearchRepository.findOne(testServiceResp.getId());
        assertThat(serviceRespEs).isEqualToComparingFieldByField(testServiceResp);*/
    }

    @Test
    @Transactional
    public void createServiceRespWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceRespRepository.findAll().size();

        // Create the ServiceResp with an existing ID
        ServiceResp existingServiceResp = new ServiceResp();
        existingServiceResp.setId(1L);
        ServiceRespDTO existingServiceRespDTO = serviceRespMapper.serviceRespToServiceRespDTO(existingServiceResp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceRespMockMvc.perform(post("/api/service-resps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingServiceRespDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ServiceResp> serviceRespList = serviceRespRepository.findAll();
        assertThat(serviceRespList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRawJsonIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRespRepository.findAll().size();
        // set the field null
        serviceResp.setRawJson(null);

        // Create the ServiceResp, which fails.
        ServiceRespDTO serviceRespDTO = serviceRespMapper.serviceRespToServiceRespDTO(serviceResp);

        restServiceRespMockMvc.perform(post("/api/service-resps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceRespDTO)))
            .andExpect(status().isBadRequest());

        List<ServiceResp> serviceRespList = serviceRespRepository.findAll();
        assertThat(serviceRespList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceResps() throws Exception {
        // Initialize the database
        serviceRespRepository.saveAndFlush(serviceResp);

        // Get all the serviceRespList
        restServiceRespMockMvc.perform(get("/api/service-resps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceResp.getId().intValue())))
            .andExpect(jsonPath("$.[*].rawJsonContentType").value(hasItem(DEFAULT_RAW_JSON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].rawJson").value(hasItem(Base64Utils.encodeToString(DEFAULT_RAW_JSON))))
            .andExpect(jsonPath("$.[*].documentImage").value(hasItem(DEFAULT_DOCUMENT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].lastRunBy").value(hasItem(DEFAULT_LAST_RUN_BY.toString())))
            .andExpect(jsonPath("$.[*].lastRunDur").value(hasItem(DEFAULT_LAST_RUN_DUR)))
            .andExpect(jsonPath("$.[*].lastRunDate").value(hasItem(sameInstant(DEFAULT_LAST_RUN_DATE))));
    }

    @Test
    @Transactional
    public void getServiceResp() throws Exception {
        // Initialize the database
        serviceRespRepository.saveAndFlush(serviceResp);

        // Get the serviceResp
        restServiceRespMockMvc.perform(get("/api/service-resps/{id}", serviceResp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceResp.getId().intValue()))
            .andExpect(jsonPath("$.rawJsonContentType").value(DEFAULT_RAW_JSON_CONTENT_TYPE))
            .andExpect(jsonPath("$.rawJson").value(Base64Utils.encodeToString(DEFAULT_RAW_JSON)))
            .andExpect(jsonPath("$.documentImage").value(DEFAULT_DOCUMENT_IMAGE.toString()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.lastRunBy").value(DEFAULT_LAST_RUN_BY.toString()))
            .andExpect(jsonPath("$.lastRunDur").value(DEFAULT_LAST_RUN_DUR))
            .andExpect(jsonPath("$.lastRunDate").value(sameInstant(DEFAULT_LAST_RUN_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingServiceResp() throws Exception {
        // Get the serviceResp
        restServiceRespMockMvc.perform(get("/api/service-resps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceResp() throws Exception {
        // Initialize the database
        serviceRespRepository.saveAndFlush(serviceResp);
        serviceRespSearchRepository.save(serviceResp);
        int databaseSizeBeforeUpdate = serviceRespRepository.findAll().size();

        // Update the serviceResp
        ServiceResp updatedServiceResp = serviceRespRepository.findOne(serviceResp.getId());
        updatedServiceResp
                .rawJson(UPDATED_RAW_JSON)
                .rawJsonContentType(UPDATED_RAW_JSON_CONTENT_TYPE)
                .documentImage(UPDATED_DOCUMENT_IMAGE)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE)
                .lastRunBy(UPDATED_LAST_RUN_BY)
                .lastRunDur(UPDATED_LAST_RUN_DUR)
                .lastRunDate(UPDATED_LAST_RUN_DATE);
        ServiceRespDTO serviceRespDTO = serviceRespMapper.serviceRespToServiceRespDTO(updatedServiceResp);

        restServiceRespMockMvc.perform(put("/api/service-resps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceRespDTO)))
            .andExpect(status().isOk());

        // Validate the ServiceResp in the database
        List<ServiceResp> serviceRespList = serviceRespRepository.findAll();
        assertThat(serviceRespList).hasSize(databaseSizeBeforeUpdate);
        ServiceResp testServiceResp = serviceRespList.get(serviceRespList.size() - 1);
        assertThat(testServiceResp.getRawJson()).isEqualTo(UPDATED_RAW_JSON);
        assertThat(testServiceResp.getRawJsonContentType()).isEqualTo(UPDATED_RAW_JSON_CONTENT_TYPE);
        assertThat(testServiceResp.getDocumentImage()).isEqualTo(UPDATED_DOCUMENT_IMAGE);
        assertThat(testServiceResp.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testServiceResp.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testServiceResp.getLastRunBy()).isEqualTo(UPDATED_LAST_RUN_BY);
        assertThat(testServiceResp.getLastRunDur()).isEqualTo(UPDATED_LAST_RUN_DUR);
        assertThat(testServiceResp.getLastRunDate()).isEqualTo(UPDATED_LAST_RUN_DATE);

        // Validate the ServiceResp in ElasticSearch
        /*ServiceResp serviceRespEs = serviceRespSearchRepository.findOne(testServiceResp.getId());
        assertThat(serviceRespEs).isEqualToComparingFieldByField(testServiceResp);*/
    }

    @Test
    @Transactional
    public void updateNonExistingServiceResp() throws Exception {
        int databaseSizeBeforeUpdate = serviceRespRepository.findAll().size();

        // Create the ServiceResp
        ServiceRespDTO serviceRespDTO = serviceRespMapper.serviceRespToServiceRespDTO(serviceResp);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restServiceRespMockMvc.perform(put("/api/service-resps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceRespDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceResp in the database
        List<ServiceResp> serviceRespList = serviceRespRepository.findAll();
        assertThat(serviceRespList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteServiceResp() throws Exception {
        // Initialize the database
        serviceRespRepository.saveAndFlush(serviceResp);
        serviceRespSearchRepository.save(serviceResp);
        int databaseSizeBeforeDelete = serviceRespRepository.findAll().size();

        // Get the serviceResp
        restServiceRespMockMvc.perform(delete("/api/service-resps/{id}", serviceResp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean serviceRespExistsInEs = serviceRespSearchRepository.exists(serviceResp.getId());
        assertThat(serviceRespExistsInEs).isFalse();

        // Validate the database is empty
        List<ServiceResp> serviceRespList = serviceRespRepository.findAll();
        assertThat(serviceRespList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServiceResp() throws Exception {
        // Initialize the database
        serviceRespRepository.saveAndFlush(serviceResp);
        serviceRespSearchRepository.save(serviceResp);

        // Search the serviceResp
        restServiceRespMockMvc.perform(get("/api/_search/service-resps?query=id:" + serviceResp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceResp.getId().intValue())))
            .andExpect(jsonPath("$.[*].rawJsonContentType").value(hasItem(DEFAULT_RAW_JSON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].rawJson").value(hasItem(Base64Utils.encodeToString(DEFAULT_RAW_JSON))))
            .andExpect(jsonPath("$.[*].documentImage").value(hasItem(DEFAULT_DOCUMENT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].lastRunBy").value(hasItem(DEFAULT_LAST_RUN_BY.toString())))
            .andExpect(jsonPath("$.[*].lastRunDur").value(hasItem(DEFAULT_LAST_RUN_DUR)))
            .andExpect(jsonPath("$.[*].lastRunDate").value(hasItem(sameInstant(DEFAULT_LAST_RUN_DATE))));
    }
}
