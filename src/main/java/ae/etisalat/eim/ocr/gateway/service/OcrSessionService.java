package ae.etisalat.eim.ocr.gateway.service;

import ae.etisalat.eim.ocr.gateway.service.dto.OcrSessionDTO;
import java.util.List;

/**
 * Service Interface for managing OcrSession.
 */
public interface OcrSessionService {

    /**
     * Save a ocrSession.
     *
     * @param ocrSessionDTO the entity to save
     * @return the persisted entity
     */
    OcrSessionDTO save(OcrSessionDTO ocrSessionDTO);

    /**
     *  Get all the ocrSessions.
     *  
     *  @return the list of entities
     */
    List<OcrSessionDTO> findAll();

    /**
     *  Get the "id" ocrSession.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    OcrSessionDTO findOne(Long id);

    /**
     *  Delete the "id" ocrSession.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ocrSession corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<OcrSessionDTO> search(String query);
}
