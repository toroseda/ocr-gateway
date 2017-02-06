package ae.etisalat.eim.ocr.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import ae.etisalat.eim.ocr.gateway.service.EdmsRequestService;
import ae.etisalat.eim.ocr.gateway.web.rest.util.HeaderUtil;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsRequestDTO;

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
 * REST controller for managing EdmsRequest.
 */
@RestController
@RequestMapping("/api")
public class EdmsRequestResource {

    private final Logger log = LoggerFactory.getLogger(EdmsRequestResource.class);
        
    @Inject
    private EdmsRequestService edmsRequestService;

    /**
     * POST  /edms-requests : Create a new edmsRequest.
     *
     * @param edmsRequestDTO the edmsRequestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new edmsRequestDTO, or with status 400 (Bad Request) if the edmsRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/edms-requests")
    @Timed
    public ResponseEntity<EdmsRequestDTO> createEdmsRequest(@Valid @RequestBody EdmsRequestDTO edmsRequestDTO) throws URISyntaxException {
        log.debug("REST request to save EdmsRequest : {}", edmsRequestDTO);
        if (edmsRequestDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("edmsRequest", "idexists", "A new edmsRequest cannot already have an ID")).body(null);
        }
        EdmsRequestDTO result = edmsRequestService.save(edmsRequestDTO);
        return ResponseEntity.created(new URI("/api/edms-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("edmsRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /edms-requests : Updates an existing edmsRequest.
     *
     * @param edmsRequestDTO the edmsRequestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated edmsRequestDTO,
     * or with status 400 (Bad Request) if the edmsRequestDTO is not valid,
     * or with status 500 (Internal Server Error) if the edmsRequestDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/edms-requests")
    @Timed
    public ResponseEntity<EdmsRequestDTO> updateEdmsRequest(@Valid @RequestBody EdmsRequestDTO edmsRequestDTO) throws URISyntaxException {
        log.debug("REST request to update EdmsRequest : {}", edmsRequestDTO);
        if (edmsRequestDTO.getId() == null) {
            return createEdmsRequest(edmsRequestDTO);
        }
        EdmsRequestDTO result = edmsRequestService.save(edmsRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("edmsRequest", edmsRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /edms-requests : get all the edmsRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of edmsRequests in body
     */
    @GetMapping("/edms-requests")
    @Timed
    public List<EdmsRequestDTO> getAllEdmsRequests() {
        log.debug("REST request to get all EdmsRequests");
        return edmsRequestService.findAll();
    }

    /**
     * GET  /edms-requests/:id : get the "id" edmsRequest.
     *
     * @param id the id of the edmsRequestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the edmsRequestDTO, or with status 404 (Not Found)
     */
    @GetMapping("/edms-requests/{id}")
    @Timed
    public ResponseEntity<EdmsRequestDTO> getEdmsRequest(@PathVariable Long id) {
        log.debug("REST request to get EdmsRequest : {}", id);
        EdmsRequestDTO edmsRequestDTO = edmsRequestService.findOne(id);
        return Optional.ofNullable(edmsRequestDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /edms-requests/:id : delete the "id" edmsRequest.
     *
     * @param id the id of the edmsRequestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/edms-requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteEdmsRequest(@PathVariable Long id) {
        log.debug("REST request to delete EdmsRequest : {}", id);
        edmsRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("edmsRequest", id.toString())).build();
    }

    /**
     * SEARCH  /_search/edms-requests?query=:query : search for the edmsRequest corresponding
     * to the query.
     *
     * @param query the query of the edmsRequest search 
     * @return the result of the search
     */
    @GetMapping("/_search/edms-requests")
    @Timed
    public List<EdmsRequestDTO> searchEdmsRequests(@RequestParam String query) {
        log.debug("REST request to search EdmsRequests for query {}", query);
        return edmsRequestService.search(query);
    }


}
