package ae.etisalat.eim.ocr.gateway.service.impl;

import ae.etisalat.eim.ocr.gateway.service.EdmsDownloadService;
import ae.etisalat.eim.ocr.gateway.domain.EdmsDownload;
import ae.etisalat.eim.ocr.gateway.repository.EdmsDownloadRepository;
import ae.etisalat.eim.ocr.gateway.repository.search.EdmsDownloadSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsDownloadDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.EdmsDownloadMapper;
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
 * Service Implementation for managing EdmsDownload.
 */
@Service
@Transactional
public class EdmsDownloadServiceImpl implements EdmsDownloadService{

    private final Logger log = LoggerFactory.getLogger(EdmsDownloadServiceImpl.class);
    
    @Inject
    private EdmsDownloadRepository edmsDownloadRepository;

    @Inject
    private EdmsDownloadMapper edmsDownloadMapper;

    @Inject
    private EdmsDownloadSearchRepository edmsDownloadSearchRepository;

    /**
     * Save a edmsDownload.
     *
     * @param edmsDownloadDTO the entity to save
     * @return the persisted entity
     */
    public EdmsDownloadDTO save(EdmsDownloadDTO edmsDownloadDTO) {
        log.debug("Request to save EdmsDownload : {}", edmsDownloadDTO);
        EdmsDownload edmsDownload = edmsDownloadMapper.edmsDownloadDTOToEdmsDownload(edmsDownloadDTO);
        edmsDownload = edmsDownloadRepository.save(edmsDownload);
        EdmsDownloadDTO result = edmsDownloadMapper.edmsDownloadToEdmsDownloadDTO(edmsDownload);
        edmsDownloadSearchRepository.save(edmsDownload);
        return result;
    }

    /**
     *  Get all the edmsDownloads.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<EdmsDownloadDTO> findAll() {
        log.debug("Request to get all EdmsDownloads");
        List<EdmsDownloadDTO> result = edmsDownloadRepository.findAll().stream()
            .map(edmsDownloadMapper::edmsDownloadToEdmsDownloadDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one edmsDownload by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EdmsDownloadDTO findOne(Long id) {
        log.debug("Request to get EdmsDownload : {}", id);
        EdmsDownload edmsDownload = edmsDownloadRepository.findOne(id);
        EdmsDownloadDTO edmsDownloadDTO = edmsDownloadMapper.edmsDownloadToEdmsDownloadDTO(edmsDownload);
        return edmsDownloadDTO;
    }

    /**
     *  Delete the  edmsDownload by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EdmsDownload : {}", id);
        edmsDownloadRepository.delete(id);
        edmsDownloadSearchRepository.delete(id);
    }

    /**
     * Search for the edmsDownload corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EdmsDownloadDTO> search(String query) {
        log.debug("Request to search EdmsDownloads for query {}", query);
        return StreamSupport
            .stream(edmsDownloadSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(edmsDownloadMapper::edmsDownloadToEdmsDownloadDTO)
            .collect(Collectors.toList());
    }
}
