package ae.etisalat.eim.ocr.gateway.service.impl;

import ae.etisalat.eim.ocr.gateway.service.SessionWfService;
import ae.etisalat.eim.ocr.gateway.domain.SessionWf;
import ae.etisalat.eim.ocr.gateway.repository.SessionWfRepository;
import ae.etisalat.eim.ocr.gateway.repository.search.SessionWfSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.SessionWfDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.SessionWfMapper;
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
 * Service Implementation for managing SessionWf.
 */
@Service
@Transactional
public class SessionWfServiceImpl implements SessionWfService{

    private final Logger log = LoggerFactory.getLogger(SessionWfServiceImpl.class);
    
    @Inject
    private SessionWfRepository sessionWfRepository;

    @Inject
    private SessionWfMapper sessionWfMapper;

    @Inject
    private SessionWfSearchRepository sessionWfSearchRepository;

    /**
     * Save a sessionWf.
     *
     * @param sessionWfDTO the entity to save
     * @return the persisted entity
     */
    public SessionWfDTO save(SessionWfDTO sessionWfDTO) {
        log.debug("Request to save SessionWf : {}", sessionWfDTO);
        SessionWf sessionWf = sessionWfMapper.sessionWfDTOToSessionWf(sessionWfDTO);
        sessionWf = sessionWfRepository.save(sessionWf);
        SessionWfDTO result = sessionWfMapper.sessionWfToSessionWfDTO(sessionWf);
        sessionWfSearchRepository.save(sessionWf);
        return result;
    }

    /**
     *  Get all the sessionWfs.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<SessionWfDTO> findAll() {
        log.debug("Request to get all SessionWfs");
        List<SessionWfDTO> result = sessionWfRepository.findAll().stream()
            .map(sessionWfMapper::sessionWfToSessionWfDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one sessionWf by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SessionWfDTO findOne(Long id) {
        log.debug("Request to get SessionWf : {}", id);
        SessionWf sessionWf = sessionWfRepository.findOne(id);
        SessionWfDTO sessionWfDTO = sessionWfMapper.sessionWfToSessionWfDTO(sessionWf);
        return sessionWfDTO;
    }

    /**
     *  Delete the  sessionWf by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SessionWf : {}", id);
        sessionWfRepository.delete(id);
        sessionWfSearchRepository.delete(id);
    }

    /**
     * Search for the sessionWf corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SessionWfDTO> search(String query) {
        log.debug("Request to search SessionWfs for query {}", query);
        return StreamSupport
            .stream(sessionWfSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(sessionWfMapper::sessionWfToSessionWfDTO)
            .collect(Collectors.toList());
    }
}
