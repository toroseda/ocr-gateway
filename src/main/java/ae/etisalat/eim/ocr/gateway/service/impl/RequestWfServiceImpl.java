package ae.etisalat.eim.ocr.gateway.service.impl;

import ae.etisalat.eim.ocr.gateway.service.RequestWfService;
import ae.etisalat.eim.ocr.gateway.domain.RequestWf;
import ae.etisalat.eim.ocr.gateway.repository.RequestWfRepository;
import ae.etisalat.eim.ocr.gateway.repository.search.RequestWfSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.RequestWfDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.RequestWfMapper;
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
 * Service Implementation for managing RequestWf.
 */
@Service
@Transactional
public class RequestWfServiceImpl implements RequestWfService{

    private final Logger log = LoggerFactory.getLogger(RequestWfServiceImpl.class);
    
    @Inject
    private RequestWfRepository requestWfRepository;

    @Inject
    private RequestWfMapper requestWfMapper;

    @Inject
    private RequestWfSearchRepository requestWfSearchRepository;

    /**
     * Save a requestWf.
     *
     * @param requestWfDTO the entity to save
     * @return the persisted entity
     */
    public RequestWfDTO save(RequestWfDTO requestWfDTO) {
        log.debug("Request to save RequestWf : {}", requestWfDTO);
        RequestWf requestWf = requestWfMapper.requestWfDTOToRequestWf(requestWfDTO);
        requestWf = requestWfRepository.save(requestWf);
        RequestWfDTO result = requestWfMapper.requestWfToRequestWfDTO(requestWf);
        requestWfSearchRepository.save(requestWf);
        return result;
    }

    /**
     *  Get all the requestWfs.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<RequestWfDTO> findAll() {
        log.debug("Request to get all RequestWfs");
        List<RequestWfDTO> result = requestWfRepository.findAll().stream()
            .map(requestWfMapper::requestWfToRequestWfDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one requestWf by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public RequestWfDTO findOne(Long id) {
        log.debug("Request to get RequestWf : {}", id);
        RequestWf requestWf = requestWfRepository.findOne(id);
        RequestWfDTO requestWfDTO = requestWfMapper.requestWfToRequestWfDTO(requestWf);
        return requestWfDTO;
    }

    /**
     *  Delete the  requestWf by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RequestWf : {}", id);
        requestWfRepository.delete(id);
        requestWfSearchRepository.delete(id);
    }

    /**
     * Search for the requestWf corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<RequestWfDTO> search(String query) {
        log.debug("Request to search RequestWfs for query {}", query);
        return StreamSupport
            .stream(requestWfSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(requestWfMapper::requestWfToRequestWfDTO)
            .collect(Collectors.toList());
    }
}
