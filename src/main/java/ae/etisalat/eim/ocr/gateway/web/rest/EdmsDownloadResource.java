package ae.etisalat.eim.ocr.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import ae.etisalat.eim.ocr.gateway.service.EdmsDownloadService;
import ae.etisalat.eim.ocr.gateway.web.rest.util.HeaderUtil;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsDownloadDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing EdmsDownload.
 */
@RestController
@RequestMapping("/api")
public class EdmsDownloadResource {

    private final Logger log = LoggerFactory.getLogger(EdmsDownloadResource.class);
        
    @Inject
    private EdmsDownloadService edmsDownloadService;

    /**
     * POST  /edms-downloads : Create a new edmsDownload.
     *
     * @param edmsDownloadDTO the edmsDownloadDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new edmsDownloadDTO, or with status 400 (Bad Request) if the edmsDownload has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/edms-downloads")
    @Timed
    public ResponseEntity<EdmsDownloadDTO> createEdmsDownload(@Valid @RequestBody EdmsDownloadDTO edmsDownloadDTO) throws URISyntaxException {
        log.debug("REST request to save EdmsDownload : {}", edmsDownloadDTO);
        if (edmsDownloadDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("edmsDownload", "idexists", "A new edmsDownload cannot already have an ID")).body(null);
        }
        EdmsDownloadDTO result = edmsDownloadService.save(edmsDownloadDTO);
        return ResponseEntity.created(new URI("/api/edms-downloads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("edmsDownload", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /edms-downloads : Updates an existing edmsDownload.
     *
     * @param edmsDownloadDTO the edmsDownloadDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated edmsDownloadDTO,
     * or with status 400 (Bad Request) if the edmsDownloadDTO is not valid,
     * or with status 500 (Internal Server Error) if the edmsDownloadDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/edms-downloads")
    @Timed
    public ResponseEntity<EdmsDownloadDTO> updateEdmsDownload(@Valid @RequestBody EdmsDownloadDTO edmsDownloadDTO) throws URISyntaxException {
        log.debug("REST request to update EdmsDownload : {}", edmsDownloadDTO);
        if (edmsDownloadDTO.getId() == null) {
            return createEdmsDownload(edmsDownloadDTO);
        }
        EdmsDownloadDTO result = edmsDownloadService.save(edmsDownloadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("edmsDownload", edmsDownloadDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /edms-downloads : get all the edmsDownloads.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of edmsDownloads in body
     */
    @GetMapping("/edms-downloads")
    @Timed
    public List<EdmsDownloadDTO> getAllEdmsDownloads() {
        log.debug("REST request to get all EdmsDownloads");
        return edmsDownloadService.findAll();
    }

    /**
     * GET  /edms-downloads/:id : get the "id" edmsDownload.
     *
     * @param id the id of the edmsDownloadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the edmsDownloadDTO, or with status 404 (Not Found)
     */
    @GetMapping("/edms-downloads/{id}")
    @Timed
    public ResponseEntity<EdmsDownloadDTO> getEdmsDownload(@PathVariable Long id) {
        log.debug("REST request to get EdmsDownload : {}", id);
        EdmsDownloadDTO edmsDownloadDTO = edmsDownloadService.findOne(id);
        return Optional.ofNullable(edmsDownloadDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /edms-downloads/:id : delete the "id" edmsDownload.
     *
     * @param id the id of the edmsDownloadDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/edms-downloads/{id}")
    @Timed
    public ResponseEntity<Void> deleteEdmsDownload(@PathVariable Long id) {
        log.debug("REST request to delete EdmsDownload : {}", id);
        edmsDownloadService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("edmsDownload", id.toString())).build();
    }

    /**
     * SEARCH  /_search/edms-downloads?query=:query : search for the edmsDownload corresponding
     * to the query.
     *
     * @param query the query of the edmsDownload search 
     * @return the result of the search
     */
    @GetMapping("/_search/edms-downloads")
    @Timed
    public List<EdmsDownloadDTO> searchEdmsDownloads(@RequestParam String query) {
        log.debug("REST request to search EdmsDownloads for query {}", query);
        return edmsDownloadService.search(query);
    }


}
