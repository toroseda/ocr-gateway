package ae.etisalat.eim.ocr.gateway.service.impl;

import ae.etisalat.eim.ocr.gateway.service.EdmsResponseService;
import ae.etisalat.eim.ocr.gateway.domain.EdmsResponse;
import ae.etisalat.eim.ocr.gateway.repository.EdmsResponseRepository;
import ae.etisalat.eim.ocr.gateway.repository.search.EdmsResponseSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsResponseDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.EdmsResponseMapper;
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
 * Service Implementation for managing EdmsResponse.
 */
@Service
@Transactional
public class EdmsResponseServiceImpl implements EdmsResponseService{

    private final Logger log = LoggerFactory.getLogger(EdmsResponseServiceImpl.class);
    
    @Inject
    private EdmsResponseRepository edmsResponseRepository;

    @Inject
    private EdmsResponseMapper edmsResponseMapper;

    @Inject
    private EdmsResponseSearchRepository edmsResponseSearchRepository;

    /**
     * Save a edmsResponse.
     *
     * @param edmsResponseDTO the entity to save
     * @return the persisted entity
     */
    public EdmsResponseDTO save(EdmsResponseDTO edmsResponseDTO) {
        log.debug("Request to save EdmsResponse : {}", edmsResponseDTO);
        EdmsResponse edmsResponse = edmsResponseMapper.edmsResponseDTOToEdmsResponse(edmsResponseDTO);
        edmsResponse = edmsResponseRepository.save(edmsResponse);
        EdmsResponseDTO result = edmsResponseMapper.edmsResponseToEdmsResponseDTO(edmsResponse);
        edmsResponseSearchRepository.save(edmsResponse);
        return result;
    }

    /**
     *  Get all the edmsResponses.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<EdmsResponseDTO> findAll() {
        log.debug("Request to get all EdmsResponses");
        List<EdmsResponseDTO> result = edmsResponseRepository.findAll().stream()
            .map(edmsResponseMapper::edmsResponseToEdmsResponseDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one edmsResponse by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EdmsResponseDTO findOne(Long id) {
        log.debug("Request to get EdmsResponse : {}", id);
        EdmsResponse edmsResponse = edmsResponseRepository.findOne(id);
        EdmsResponseDTO edmsResponseDTO = edmsResponseMapper.edmsResponseToEdmsResponseDTO(edmsResponse);
        return edmsResponseDTO;
    }

    /**
     *  Delete the  edmsResponse by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EdmsResponse : {}", id);
        edmsResponseRepository.delete(id);
        edmsResponseSearchRepository.delete(id);
    }

    /**
     * Search for the edmsResponse corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EdmsResponseDTO> search(String query) {
        log.debug("Request to search EdmsResponses for query {}", query);
        return StreamSupport
            .stream(edmsResponseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(edmsResponseMapper::edmsResponseToEdmsResponseDTO)
            .collect(Collectors.toList());
    }
}
