package ae.etisalat.eim.ocr.gateway.service;

import ae.etisalat.eim.ocr.gateway.service.dto.EdmsRequestDTO;
import java.util.List;

/**
 * Service Interface for managing EdmsRequest.
 */
public interface EdmsRequestService {

    /**
     * Save a edmsRequest.
     *
     * @param edmsRequestDTO the entity to save
     * @return the persisted entity
     */
    EdmsRequestDTO save(EdmsRequestDTO edmsRequestDTO);

    /**
     *  Get all the edmsRequests.
     *  
     *  @return the list of entities
     */
    List<EdmsRequestDTO> findAll();

    /**
     *  Get the "id" edmsRequest.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EdmsRequestDTO findOne(Long id);

    /**
     *  Delete the "id" edmsRequest.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the edmsRequest corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<EdmsRequestDTO> search(String query);
}
