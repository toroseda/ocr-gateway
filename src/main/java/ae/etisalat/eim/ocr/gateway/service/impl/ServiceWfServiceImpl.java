package ae.etisalat.eim.ocr.gateway.service.impl;

import ae.etisalat.eim.ocr.gateway.service.ServiceWfService;
import ae.etisalat.eim.ocr.gateway.domain.ServiceWf;
import ae.etisalat.eim.ocr.gateway.repository.ServiceWfRepository;
import ae.etisalat.eim.ocr.gateway.repository.search.ServiceWfSearchRepository;
import ae.etisalat.eim.ocr.gateway.service.dto.ServiceWfDTO;
import ae.etisalat.eim.ocr.gateway.service.mapper.ServiceWfMapper;
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
 * Service Implementation for managing ServiceWf.
 */
@Service
@Transactional
public class ServiceWfServiceImpl implements ServiceWfService{

    private final Logger log = LoggerFactory.getLogger(ServiceWfServiceImpl.class);
    
    @Inject
    private ServiceWfRepository serviceWfRepository;

    @Inject
    private ServiceWfMapper serviceWfMapper;

    @Inject
    private ServiceWfSearchRepository serviceWfSearchRepository;

    /**
     * Save a serviceWf.
     *
     * @param serviceWfDTO the entity to save
     * @return the persisted entity
     */
    public ServiceWfDTO save(ServiceWfDTO serviceWfDTO) {
        log.debug("Request to save ServiceWf : {}", serviceWfDTO);
        ServiceWf serviceWf = serviceWfMapper.serviceWfDTOToServiceWf(serviceWfDTO);
        serviceWf = serviceWfRepository.save(serviceWf);
        ServiceWfDTO result = serviceWfMapper.serviceWfToServiceWfDTO(serviceWf);
        serviceWfSearchRepository.save(serviceWf);
        return result;
    }

    /**
     *  Get all the serviceWfs.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ServiceWfDTO> findAll() {
        log.debug("Request to get all ServiceWfs");
        List<ServiceWfDTO> result = serviceWfRepository.findAll().stream()
            .map(serviceWfMapper::serviceWfToServiceWfDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one serviceWf by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ServiceWfDTO findOne(Long id) {
        log.debug("Request to get ServiceWf : {}", id);
        ServiceWf serviceWf = serviceWfRepository.findOne(id);
        ServiceWfDTO serviceWfDTO = serviceWfMapper.serviceWfToServiceWfDTO(serviceWf);
        return serviceWfDTO;
    }

    /**
     *  Delete the  serviceWf by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceWf : {}", id);
        serviceWfRepository.delete(id);
        serviceWfSearchRepository.delete(id);
    }

    /**
     * Search for the serviceWf corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ServiceWfDTO> search(String query) {
        log.debug("Request to search ServiceWfs for query {}", query);
        return StreamSupport
            .stream(serviceWfSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(serviceWfMapper::serviceWfToServiceWfDTO)
            .collect(Collectors.toList());
    }
}
