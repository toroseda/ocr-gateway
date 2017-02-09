package ae.etisalat.eim.ocr.gateway.service.impl;

import ae.etisalat.eim.ocr.gateway.service.OcrSessionService;
import ae.etisalat.eim.ocr.gateway.domain.EdmsRequest;
import ae.etisalat.eim.ocr.gateway.domain.OcrSession;
import ae.etisalat.eim.ocr.gateway.domain.RequestWf;
import ae.etisalat.eim.ocr.gateway.domain.SessionWf;
import ae.etisalat.eim.ocr.gateway.domain.enumeration.Status;
import ae.etisalat.eim.ocr.gateway.domain.enumeration.WfStatus;
import ae.etisalat.eim.ocr.gateway.repository.EdmsRequestRepository;
import ae.etisalat.eim.ocr.gateway.repository.OcrSessionRepository;
import ae.etisalat.eim.ocr.gateway.repository.RequestWfRepository;
import ae.etisalat.eim.ocr.gateway.repository.SessionWfRepository;
import ae.etisalat.eim.ocr.gateway.repository.search.EdmsRequestSearchRepository;
import ae.etisalat.eim.ocr.gateway.repository.search.OcrSessionSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.OcrSessionDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.EdmsRequestMapper;
import ae.etisalat.eim.ocr.gateway.service.mapper.OcrSessionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OcrSession.
 */
@Service
@Transactional
public class OcrSessionServiceImpl implements OcrSessionService{

    private final Logger log = LoggerFactory.getLogger(OcrSessionServiceImpl.class);
    
    @Inject
    private OcrSessionRepository ocrSessionRepository;

    @Inject
    private OcrSessionMapper ocrSessionMapper;

    @Inject
    private OcrSessionSearchRepository ocrSessionSearchRepository;
    
    /*
     * Introduce also the injections for the EDMSRequest in here. This means we will validate, the request
     * entries before storing them against the new OCRSession DTO we wish to save. 
     * 
     * No rest calls will be made directly as we have the service implementation for the EdmsRequest object to hand
     * also.
     */
    
    @Inject
    private RequestWfRepository requestWfRepository;
    
    @Inject SessionWfRepository sessionWfRepository;
    
    @Inject
    private EdmsRequestRepository edmsRequestRepository;

    @Inject
    private EdmsRequestMapper edmsRequestMapper;

    @Inject
    private EdmsRequestSearchRepository edmsRequestSearchRepository;
    
    

    /**
     * Save a ocrSession.
     *
     * @param ocrSessionDTO the entity to save
     * @return the persisted entity
     */
    public OcrSessionDTO save(OcrSessionDTO ocrSessionDTO) {
        log.debug("Request to save OcrSession : {}", ocrSessionDTO);
        
        OcrSessionDTO result = null;
        OcrSession ocrSession = ocrSessionMapper.ocrSessionDTOToOcrSession(ocrSessionDTO);
        
        // The OcrSession also has the Base64 data for the request as an attribute. Need to get it out. 
        ocrSession = ocrSessionRepository.save(ocrSession);
        
        SessionWf sessionWf = new SessionWf();
        
        // Create a new OCR Session Workflow Status
        sessionWf.setOcrSession(ocrSession);
        sessionWf.setWfStatus(WfStatus.READY);
        sessionWf.setStatus(Status.LOADED);
        
        // Save this workflow status of this session
        sessionWf = sessionWfRepository.save(sessionWf);
        
        // Then save the EdmsRequest part specifically for this session also.
        ByteArrayInputStream bais = new ByteArrayInputStream(ocrSession.getRequestData());
        InputStreamReader inStreamReader = new InputStreamReader(bais);
        BufferedReader buffReader = new BufferedReader(inStreamReader);
        
        String line = null;
        EdmsRequest edmsRequest = null;
        RequestWf requestWorkflow = null;
        ArrayList<EdmsRequest> edmsRequestList = new ArrayList<>();
        ArrayList<RequestWf> requestWorkflowList = new ArrayList<>();
        
        long countLines = 0;
        long countValidRequests = 0;
        
        try {
        	while ((line = buffReader.readLine()) != null) {
        		if (line.startsWith("#")){
        			continue;
        		}
        		countLines++;
        		
        		if ( countLines % 10L == 0){
        			log.debug(line);
        		}
        		
        		// Split the line based on csv fields
        		String[] requestParts = line.split(",");
        		
        		if (requestParts.length > 2){
        			// Translate into an EdmsRequest as required
            		edmsRequest = new EdmsRequest();
            		edmsRequest.setAccountNumber(requestParts[0].trim()); // Account number or MSISDN
            		edmsRequest.setSubRequestId(requestParts[1].trim());  // Sub-request Id
            		edmsRequest.setAreaCode(requestParts[2].trim()); 		// Area Code
            		
            		// add ocrSession
            		edmsRequest.setOcrSession(ocrSession);
            		
            		// Add to the list Edms Request list
            		edmsRequestList.add(edmsRequest);
            		
            		// Add the workflow status by creating a new workflow for requests and adding the request into it
            		requestWorkflow = new RequestWf();
            		requestWorkflow.setEdmsRequest(edmsRequest);
            		requestWorkflow.setStatus(Status.LOADED); // Requests are loaded and ready to execute
            		requestWorkflow.setWfStatus(WfStatus.READY); // Workflow status only starts once the workflow start is triggered
            		
            		// Add to the workflow  list
            		requestWorkflowList.add(requestWorkflow);
            		
            		// Count it
            		countValidRequests++;
        		}   		
        		
        		// Here check if the validation checks are okay and then save the requests in the requests table. 
        		// TBD - Otherwise need to fail it or send a warning signal e.g. requests ratio to number of requests is too low or use hard count. 
        		
        		// Add the requests to the table by iterating the arraylist
        		edmsRequestRepository.save(edmsRequestList);
        		
        		// Add the status to the Workflow request table by also saving
        		requestWfRepository.save(requestWorkflowList);
        		
        	}
        	
        } catch ( IOException ioe){
        	ioe.printStackTrace();
        }
        
        log.debug("\n\n\n\n\n" + countLines + "\n\n\n\n\n");
        
        result = ocrSessionMapper.ocrSessionToOcrSessionDTO(ocrSession);
        ocrSessionSearchRepository.save(ocrSession);
        return result;
    }

    /**
     *  Get all the ocrSessions.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<OcrSessionDTO> findAll() {
        log.debug("Request to get all OcrSessions");
        List<OcrSessionDTO> result = ocrSessionRepository.findAll().stream()
            .map(ocrSessionMapper::ocrSessionToOcrSessionDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one ocrSession by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OcrSessionDTO findOne(Long id) {
        log.debug("Request to get OcrSession : {}", id);
        OcrSession ocrSession = ocrSessionRepository.findOne(id);
        OcrSessionDTO ocrSessionDTO = ocrSessionMapper.ocrSessionToOcrSessionDTO(ocrSession);
        return ocrSessionDTO;
    }

    /**
     *  Delete the  ocrSession by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OcrSession : {}", id);
        ocrSessionRepository.delete(id);
        ocrSessionSearchRepository.delete(id);
    }

    /**
     * Search for the ocrSession corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OcrSessionDTO> search(String query) {
        log.debug("Request to search OcrSessions for query {}", query);
        return StreamSupport
            .stream(ocrSessionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(ocrSessionMapper::ocrSessionToOcrSessionDTO)
            .collect(Collectors.toList());
    }
}
