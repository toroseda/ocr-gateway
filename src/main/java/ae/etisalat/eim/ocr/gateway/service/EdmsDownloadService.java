package ae.etisalat.eim.ocr.gateway.service;

import ae.etisalat.eim.ocr.gateway.service.dto.EdmsDownloadDTO;
import java.util.List;

/**
 * Service Interface for managing EdmsDownload.
 */
public interface EdmsDownloadService {

    /**
     * Save a edmsDownload.
     *
     * @param edmsDownloadDTO the entity to save
     * @return the persisted entity
     */
    EdmsDownloadDTO save(EdmsDownloadDTO edmsDownloadDTO);

    /**
     *  Get all the edmsDownloads.
     *  
     *  @return the list of entities
     */
    List<EdmsDownloadDTO> findAll();

    /**
     *  Get the "id" edmsDownload.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EdmsDownloadDTO findOne(Long id);

    /**
     *  Delete the "id" edmsDownload.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the edmsDownload corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<EdmsDownloadDTO> search(String query);
}
