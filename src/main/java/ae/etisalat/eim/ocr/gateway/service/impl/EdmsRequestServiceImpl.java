package ae.etisalat.eim.ocr.gateway.service.impl;

import ae.etisalat.eim.ocr.gateway.service.EdmsRequestService;
import ae.etisalat.eim.ocr.gateway.domain.EdmsRequest;
import ae.etisalat.eim.ocr.gateway.repository.EdmsRequestRepository;
import ae.etisalat.eim.ocr.gateway.repository.search.EdmsRequestSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsRequestDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.EdmsRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing EdmsRequest.
 */
@Service
@Transactional
public class EdmsRequestServiceImpl implements EdmsRequestService{

    private final Logger log = LoggerFactory.getLogger(EdmsRequestServiceImpl.class);
    
    @Inject
    private EdmsRequestRepository edmsRequestRepository;

    @Inject
    private EdmsRequestMapper edmsRequestMapper;

    @Inject
    private EdmsRequestSearchRepository edmsRequestSearchRepository;

    /**
     * Save a edmsRequest.
     *
     * @param edmsRequestDTO the entity to save
     * @return the persisted entity
     */
    public EdmsRequestDTO save(EdmsRequestDTO edmsRequestDTO) {
        log.debug("Request to save EdmsRequest : {}", edmsRequestDTO);
        EdmsRequest edmsRequest = edmsRequestMapper.edmsRequestDTOToEdmsRequest(edmsRequestDTO);
        edmsRequest = edmsRequestRepository.save(edmsRequest);
        EdmsRequestDTO result = edmsRequestMapper.edmsRequestToEdmsRequestDTO(edmsRequest);
        edmsRequestSearchRepository.save(edmsRequest);
        return result;
    }

    /**
     *  Get all the edmsRequests.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<EdmsRequestDTO> findAll() {
        log.debug("Request to get all EdmsRequests");
        List<EdmsRequestDTO> result = edmsRequestRepository.findAll().stream()
            .map(edmsRequestMapper::edmsRequestToEdmsRequestDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one edmsRequest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EdmsRequestDTO findOne(Long id) {
        log.debug("Request to get EdmsRequest : {}", id);
        EdmsRequest edmsRequest = edmsRequestRepository.findOne(id);
        EdmsRequestDTO edmsRequestDTO = edmsRequestMapper.edmsRequestToEdmsRequestDTO(edmsRequest);
        return edmsRequestDTO;
    }

    /**
     *  Delete the  edmsRequest by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EdmsRequest : {}", id);
        edmsRequestRepository.delete(id);
        edmsRequestSearchRepository.delete(id);
    }

    /**
     * Search for the edmsRequest corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EdmsRequestDTO> search(String query) {
        log.debug("Request to search EdmsRequests for query {}", query);
        return StreamSupport
            .stream(edmsRequestSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(edmsRequestMapper::edmsRequestToEdmsRequestDTO)
            .collect(Collectors.toList());
    }
}
