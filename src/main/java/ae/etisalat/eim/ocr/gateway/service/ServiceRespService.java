package ae.etisalat.eim.ocr.gateway.service;

import ae.etisalat.eim.ocr.gateway.service.dto.ServiceRespDTO;
import java.util.List;

/**
 * Service Interface for managing ServiceResp.
 */
public interface ServiceRespService {

    /**
     * Save a serviceResp.
     *
     * @param serviceRespDTO the entity to save
     * @return the persisted entity
     */
    ServiceRespDTO save(ServiceRespDTO serviceRespDTO);

    /**
     *  Get all the serviceResps.
     *  
     *  @return the list of entities
     */
    List<ServiceRespDTO> findAll();

    /**
     *  Get the "id" serviceResp.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ServiceRespDTO findOne(Long id);

    /**
     *  Delete the "id" serviceResp.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the serviceResp corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<ServiceRespDTO> search(String query);
}
