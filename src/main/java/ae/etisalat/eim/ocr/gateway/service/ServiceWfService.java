package ae.etisalat.eim.ocr.gateway.service;

import ae.etisalat.eim.ocr.gateway.service.dto.ServiceWfDTO;
import java.util.List;

/**
 * Service Interface for managing ServiceWf.
 */
public interface ServiceWfService {

    /**
     * Save a serviceWf.
     *
     * @param serviceWfDTO the entity to save
     * @return the persisted entity
     */
    ServiceWfDTO save(ServiceWfDTO serviceWfDTO);

    /**
     *  Get all the serviceWfs.
     *  
     *  @return the list of entities
     */
    List<ServiceWfDTO> findAll();

    /**
     *  Get the "id" serviceWf.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ServiceWfDTO findOne(Long id);

    /**
     *  Delete the "id" serviceWf.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the serviceWf corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<ServiceWfDTO> search(String query);
}
