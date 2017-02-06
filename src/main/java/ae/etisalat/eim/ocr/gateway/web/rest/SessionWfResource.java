package ae.etisalat.eim.ocr.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import ae.etisalat.eim.ocr.gateway.service.SessionWfService;
import ae.etisalat.eim.ocr.gateway.web.rest.util.HeaderUtil;
import ae.etisalat.eim.ocr.gateway.service.dto.SessionWfDTO;

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
 * REST controller for managing SessionWf.
 */
@RestController
@RequestMapping("/api")
public class SessionWfResource {

    private final Logger log = LoggerFactory.getLogger(SessionWfResource.class);
        
    @Inject
    private SessionWfService sessionWfService;

    /**
     * POST  /session-wfs : Create a new sessionWf.
     *
     * @param sessionWfDTO the sessionWfDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sessionWfDTO, or with status 400 (Bad Request) if the sessionWf has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/session-wfs")
    @Timed
    public ResponseEntity<SessionWfDTO> createSessionWf(@Valid @RequestBody SessionWfDTO sessionWfDTO) throws URISyntaxException {
        log.debug("REST request to save SessionWf : {}", sessionWfDTO);
        if (sessionWfDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sessionWf", "idexists", "A new sessionWf cannot already have an ID")).body(null);
        }
        SessionWfDTO result = sessionWfService.save(sessionWfDTO);
        return ResponseEntity.created(new URI("/api/session-wfs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sessionWf", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /session-wfs : Updates an existing sessionWf.
     *
     * @param sessionWfDTO the sessionWfDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sessionWfDTO,
     * or with status 400 (Bad Request) if the sessionWfDTO is not valid,
     * or with status 500 (Internal Server Error) if the sessionWfDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/session-wfs")
    @Timed
    public ResponseEntity<SessionWfDTO> updateSessionWf(@Valid @RequestBody SessionWfDTO sessionWfDTO) throws URISyntaxException {
        log.debug("REST request to update SessionWf : {}", sessionWfDTO);
        if (sessionWfDTO.getId() == null) {
            return createSessionWf(sessionWfDTO);
        }
        SessionWfDTO result = sessionWfService.save(sessionWfDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sessionWf", sessionWfDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /session-wfs : get all the sessionWfs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sessionWfs in body
     */
    @GetMapping("/session-wfs")
    @Timed
    public List<SessionWfDTO> getAllSessionWfs() {
        log.debug("REST request to get all SessionWfs");
        return sessionWfService.findAll();
    }

    /**
     * GET  /session-wfs/:id : get the "id" sessionWf.
     *
     * @param id the id of the sessionWfDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sessionWfDTO, or with status 404 (Not Found)
     */
    @GetMapping("/session-wfs/{id}")
    @Timed
    public ResponseEntity<SessionWfDTO> getSessionWf(@PathVariable Long id) {
        log.debug("REST request to get SessionWf : {}", id);
        SessionWfDTO sessionWfDTO = sessionWfService.findOne(id);
        return Optional.ofNullable(sessionWfDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /session-wfs/:id : delete the "id" sessionWf.
     *
     * @param id the id of the sessionWfDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/session-wfs/{id}")
    @Timed
    public ResponseEntity<Void> deleteSessionWf(@PathVariable Long id) {
        log.debug("REST request to delete SessionWf : {}", id);
        sessionWfService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sessionWf", id.toString())).build();
    }

    /**
     * SEARCH  /_search/session-wfs?query=:query : search for the sessionWf corresponding
     * to the query.
     *
     * @param query the query of the sessionWf search 
     * @return the result of the search
     */
    @GetMapping("/_search/session-wfs")
    @Timed
    public List<SessionWfDTO> searchSessionWfs(@RequestParam String query) {
        log.debug("REST request to search SessionWfs for query {}", query);
        return sessionWfService.search(query);
    }


}
