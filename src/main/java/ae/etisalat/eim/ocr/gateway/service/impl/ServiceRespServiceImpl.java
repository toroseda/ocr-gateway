package ae.etisalat.eim.ocr.gateway.service.impl;

import ae.etisalat.eim.ocr.gateway.service.ServiceRespService;
import ae.etisalat.eim.ocr.gateway.domain.ServiceResp;
import ae.etisalat.eim.ocr.gateway.repository.ServiceRespRepository;
import ae.etisalat.eim.ocr.gateway.repository.search.ServiceRespSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.ServiceRespDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.ServiceRespMapper;
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
 * Service Implementation for managing ServiceResp.
 */
@Service
@Transactional
public class ServiceRespServiceImpl implements ServiceRespService{

    private final Logger log = LoggerFactory.getLogger(ServiceRespServiceImpl.class);
    
    @Inject
    private ServiceRespRepository serviceRespRepository;

    @Inject
    private ServiceRespMapper serviceRespMapper;

    @Inject
    private ServiceRespSearchRepository serviceRespSearchRepository;

    /**
     * Save a serviceResp.
     *
     * @param serviceRespDTO the entity to save
     * @return the persisted entity
     */
    public ServiceRespDTO save(ServiceRespDTO serviceRespDTO) {
        log.debug("Request to save ServiceResp : {}", serviceRespDTO);
        ServiceResp serviceResp = serviceRespMapper.serviceRespDTOToServiceResp(serviceRespDTO);
        serviceResp = serviceRespRepository.save(serviceResp);
        ServiceRespDTO result = serviceRespMapper.serviceRespToServiceRespDTO(serviceResp);
        serviceRespSearchRepository.save(serviceResp);
        return result;
    }

    /**
     *  Get all the serviceResps.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ServiceRespDTO> findAll() {
        log.debug("Request to get all ServiceResps");
        List<ServiceRespDTO> result = serviceRespRepository.findAll().stream()
            .map(serviceRespMapper::serviceRespToServiceRespDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one serviceResp by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ServiceRespDTO findOne(Long id) {
        log.debug("Request to get ServiceResp : {}", id);
        ServiceResp serviceResp = serviceRespRepository.findOne(id);
        ServiceRespDTO serviceRespDTO = serviceRespMapper.serviceRespToServiceRespDTO(serviceResp);
        return serviceRespDTO;
    }

    /**
     *  Delete the  serviceResp by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceResp : {}", id);
        serviceRespRepository.delete(id);
        serviceRespSearchRepository.delete(id);
    }

    /**
     * Search for the serviceResp corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ServiceRespDTO> search(String query) {
        log.debug("Request to search ServiceResps for query {}", query);
        return StreamSupport
            .stream(serviceRespSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(serviceRespMapper::serviceRespToServiceRespDTO)
            .collect(Collectors.toList());
    }
}
