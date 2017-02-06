package ae.etisalat.eim.ocr.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import ae.etisalat.eim.ocr.gateway.service.OcrSessionService;
import ae.etisalat.eim.ocr.gateway.web.rest.util.HeaderUtil;
import ae.etisalat.eim.ocr.gateway.service.dto.OcrSessionDTO;

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
 * REST controller for managing OcrSession.
 */
@RestController
@RequestMapping("/api")
public class OcrSessionResource {

    private final Logger log = LoggerFactory.getLogger(OcrSessionResource.class);
        
    @Inject
    private OcrSessionService ocrSessionService;

    /**
     * POST  /ocr-sessions : Create a new ocrSession.
     *
     * @param ocrSessionDTO the ocrSessionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ocrSessionDTO, or with status 400 (Bad Request) if the ocrSession has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ocr-sessions")
    @Timed
    public ResponseEntity<OcrSessionDTO> createOcrSession(@Valid @RequestBody OcrSessionDTO ocrSessionDTO) throws URISyntaxException {
        log.debug("REST request to save OcrSession : {}", ocrSessionDTO);
        if (ocrSessionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ocrSession", "idexists", "A new ocrSession cannot already have an ID")).body(null);
        }
        OcrSessionDTO result = ocrSessionService.save(ocrSessionDTO);
        return ResponseEntity.created(new URI("/api/ocr-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ocrSession", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ocr-sessions : Updates an existing ocrSession.
     *
     * @param ocrSessionDTO the ocrSessionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ocrSessionDTO,
     * or with status 400 (Bad Request) if the ocrSessionDTO is not valid,
     * or with status 500 (Internal Server Error) if the ocrSessionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ocr-sessions")
    @Timed
    public ResponseEntity<OcrSessionDTO> updateOcrSession(@Valid @RequestBody OcrSessionDTO ocrSessionDTO) throws URISyntaxException {
        log.debug("REST request to update OcrSession : {}", ocrSessionDTO);
        if (ocrSessionDTO.getId() == null) {
            return createOcrSession(ocrSessionDTO);
        }
        OcrSessionDTO result = ocrSessionService.save(ocrSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ocrSession", ocrSessionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ocr-sessions : get all the ocrSessions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ocrSessions in body
     */
    @GetMapping("/ocr-sessions")
    @Timed
    public List<OcrSessionDTO> getAllOcrSessions() {
        log.debug("REST request to get all OcrSessions");
        return ocrSessionService.findAll();
    }

    /**
     * GET  /ocr-sessions/:id : get the "id" ocrSession.
     *
     * @param id the id of the ocrSessionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ocrSessionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ocr-sessions/{id}")
    @Timed
    public ResponseEntity<OcrSessionDTO> getOcrSession(@PathVariable Long id) {
        log.debug("REST request to get OcrSession : {}", id);
        OcrSessionDTO ocrSessionDTO = ocrSessionService.findOne(id);
        return Optional.ofNullable(ocrSessionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ocr-sessions/:id : delete the "id" ocrSession.
     *
     * @param id the id of the ocrSessionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ocr-sessions/{id}")
    @Timed
    public ResponseEntity<Void> deleteOcrSession(@PathVariable Long id) {
        log.debug("REST request to delete OcrSession : {}", id);
        ocrSessionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ocrSession", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ocr-sessions?query=:query : search for the ocrSession corresponding
     * to the query.
     *
     * @param query the query of the ocrSession search 
     * @return the result of the search
     */
    @GetMapping("/_search/ocr-sessions")
    @Timed
    public List<OcrSessionDTO> searchOcrSessions(@RequestParam String query) {
        log.debug("REST request to search OcrSessions for query {}", query);
        return ocrSessionService.search(query);
    }


}
