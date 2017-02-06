package ae.etisalat.eim.ocr.gateway.service.impl;

import ae.etisalat.eim.ocr.gateway.service.OcrSessionService;
import ae.etisalat.eim.ocr.gateway.domain.OcrSession;
import ae.etisalat.eim.ocr.gateway.repository.OcrSessionRepository;
import ae.etisalat.eim.ocr.gateway.repository.search.OcrSessionSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.OcrSessionDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.OcrSessionMapper;
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

    /**
     * Save a ocrSession.
     *
     * @param ocrSessionDTO the entity to save
     * @return the persisted entity
     */
    public OcrSessionDTO save(OcrSessionDTO ocrSessionDTO) {
        log.debug("Request to save OcrSession : {}", ocrSessionDTO);
        OcrSession ocrSession = ocrSessionMapper.ocrSessionDTOToOcrSession(ocrSessionDTO);
        ocrSession = ocrSessionRepository.save(ocrSession);
        OcrSessionDTO result = ocrSessionMapper.ocrSessionToOcrSessionDTO(ocrSession);
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
