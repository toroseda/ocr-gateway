package ae.etisalat.eim.ocr.gateway.service;

import ae.etisalat.eim.ocr.gateway.service.dto.SessionWfDTO;
import java.util.List;

/**
 * Service Interface for managing SessionWf.
 */
public interface SessionWfService {

    /**
     * Save a sessionWf.
     *
     * @param sessionWfDTO the entity to save
     * @return the persisted entity
     */
    SessionWfDTO save(SessionWfDTO sessionWfDTO);

    /**
     *  Get all the sessionWfs.
     *  
     *  @return the list of entities
     */
    List<SessionWfDTO> findAll();

    /**
     *  Get the "id" sessionWf.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SessionWfDTO findOne(Long id);

    /**
     *  Delete the "id" sessionWf.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sessionWf corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<SessionWfDTO> search(String query);
}
