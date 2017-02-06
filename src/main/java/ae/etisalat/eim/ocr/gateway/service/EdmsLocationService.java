package ae.etisalat.eim.ocr.gateway.service;

import ae.etisalat.eim.ocr.gateway.service.dto.EdmsLocationDTO;
import java.util.List;

/**
 * Service Interface for managing EdmsLocation.
 */
public interface EdmsLocationService {

    /**
     * Save a edmsLocation.
     *
     * @param edmsLocationDTO the entity to save
     * @return the persisted entity
     */
    EdmsLocationDTO save(EdmsLocationDTO edmsLocationDTO);

    /**
     *  Get all the edmsLocations.
     *  
     *  @return the list of entities
     */
    List<EdmsLocationDTO> findAll();

    /**
     *  Get the "id" edmsLocation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EdmsLocationDTO findOne(Long id);

    /**
     *  Delete the "id" edmsLocation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the edmsLocation corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<EdmsLocationDTO> search(String query);
}
