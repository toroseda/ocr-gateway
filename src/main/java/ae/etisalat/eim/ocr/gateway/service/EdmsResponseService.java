package ae.etisalat.eim.ocr.gateway.service;

import ae.etisalat.eim.ocr.gateway.service.dto.EdmsResponseDTO;
import java.util.List;

/**
 * Service Interface for managing EdmsResponse.
 */
public interface EdmsResponseService {

    /**
     * Save a edmsResponse.
     *
     * @param edmsResponseDTO the entity to save
     * @return the persisted entity
     */
    EdmsResponseDTO save(EdmsResponseDTO edmsResponseDTO);

    /**
     *  Get all the edmsResponses.
     *  
     *  @return the list of entities
     */
    List<EdmsResponseDTO> findAll();

    /**
     *  Get the "id" edmsResponse.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EdmsResponseDTO findOne(Long id);

    /**
     *  Delete the "id" edmsResponse.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the edmsResponse corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<EdmsResponseDTO> search(String query);
}
