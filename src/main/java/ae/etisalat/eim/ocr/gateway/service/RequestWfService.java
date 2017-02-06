package ae.etisalat.eim.ocr.gateway.service;

import ae.etisalat.eim.ocr.gateway.service.dto.RequestWfDTO;
import java.util.List;

/**
 * Service Interface for managing RequestWf.
 */
public interface RequestWfService {

    /**
     * Save a requestWf.
     *
     * @param requestWfDTO the entity to save
     * @return the persisted entity
     */
    RequestWfDTO save(RequestWfDTO requestWfDTO);

    /**
     *  Get all the requestWfs.
     *  
     *  @return the list of entities
     */
    List<RequestWfDTO> findAll();

    /**
     *  Get the "id" requestWf.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RequestWfDTO findOne(Long id);

    /**
     *  Delete the "id" requestWf.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the requestWf corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<RequestWfDTO> search(String query);
}
