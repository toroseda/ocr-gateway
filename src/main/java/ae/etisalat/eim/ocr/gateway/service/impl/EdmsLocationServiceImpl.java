package ae.etisalat.eim.ocr.gateway.service.impl;

import ae.etisalat.eim.ocr.gateway.service.EdmsLocationService;
import ae.etisalat.eim.ocr.gateway.domain.EdmsLocation;
import ae.etisalat.eim.ocr.gateway.repository.EdmsLocationRepository;
import ae.etisalat.eim.ocr.gateway.repository.search.EdmsLocationSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsLocationDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.EdmsLocationMapper;
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
 * Service Implementation for managing EdmsLocation.
 */
@Service
@Transactional
public class EdmsLocationServiceImpl implements EdmsLocationService{

    private final Logger log = LoggerFactory.getLogger(EdmsLocationServiceImpl.class);
    
    @Inject
    private EdmsLocationRepository edmsLocationRepository;

    @Inject
    private EdmsLocationMapper edmsLocationMapper;

    @Inject
    private EdmsLocationSearchRepository edmsLocationSearchRepository;

    /**
     * Save a edmsLocation.
     *
     * @param edmsLocationDTO the entity to save
     * @return the persisted entity
     */
    public EdmsLocationDTO save(EdmsLocationDTO edmsLocationDTO) {
        log.debug("Request to save EdmsLocation : {}", edmsLocationDTO);
        EdmsLocation edmsLocation = edmsLocationMapper.edmsLocationDTOToEdmsLocation(edmsLocationDTO);
        edmsLocation = edmsLocationRepository.save(edmsLocation);
        EdmsLocationDTO result = edmsLocationMapper.edmsLocationToEdmsLocationDTO(edmsLocation);
        edmsLocationSearchRepository.save(edmsLocation);
        return result;
    }

    /**
     *  Get all the edmsLocations.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<EdmsLocationDTO> findAll() {
        log.debug("Request to get all EdmsLocations");
        List<EdmsLocationDTO> result = edmsLocationRepository.findAll().stream()
            .map(edmsLocationMapper::edmsLocationToEdmsLocationDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one edmsLocation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EdmsLocationDTO findOne(Long id) {
        log.debug("Request to get EdmsLocation : {}", id);
        EdmsLocation edmsLocation = edmsLocationRepository.findOne(id);
        EdmsLocationDTO edmsLocationDTO = edmsLocationMapper.edmsLocationToEdmsLocationDTO(edmsLocation);
        return edmsLocationDTO;
    }

    /**
     *  Delete the  edmsLocation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EdmsLocation : {}", id);
        edmsLocationRepository.delete(id);
        edmsLocationSearchRepository.delete(id);
    }

    /**
     * Search for the edmsLocation corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EdmsLocationDTO> search(String query) {
        log.debug("Request to search EdmsLocations for query {}", query);
        return StreamSupport
            .stream(edmsLocationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(edmsLocationMapper::edmsLocationToEdmsLocationDTO)
            .collect(Collectors.toList());
    }
}
