package ae.etisalat.eim.ocr.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import ae.etisalat.eim.ocr.gateway.service.EdmsResponseService;
import ae.etisalat.eim.ocr.gateway.web.rest.util.HeaderUtil;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsResponseDTO;

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
 * REST controller for managing EdmsResponse.
 */
@RestController
@RequestMapping("/api")
public class EdmsResponseResource {

    private final Logger log = LoggerFactory.getLogger(EdmsResponseResource.class);
        
    @Inject
    private EdmsResponseService edmsResponseService;

    /**
     * POST  /edms-responses : Create a new edmsResponse.
     *
     * @param edmsResponseDTO the edmsResponseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new edmsResponseDTO, or with status 400 (Bad Request) if the edmsResponse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/edms-responses")
    @Timed
    public ResponseEntity<EdmsResponseDTO> createEdmsResponse(@Valid @RequestBody EdmsResponseDTO edmsResponseDTO) throws URISyntaxException {
        log.debug("REST request to save EdmsResponse : {}", edmsResponseDTO);
        if (edmsResponseDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("edmsResponse", "idexists", "A new edmsResponse cannot already have an ID")).body(null);
        }
        EdmsResponseDTO result = edmsResponseService.save(edmsResponseDTO);
        return ResponseEntity.created(new URI("/api/edms-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("edmsResponse", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /edms-responses : Updates an existing edmsResponse.
     *
     * @param edmsResponseDTO the edmsResponseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated edmsResponseDTO,
     * or with status 400 (Bad Request) if the edmsResponseDTO is not valid,
     * or with status 500 (Internal Server Error) if the edmsResponseDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/edms-responses")
    @Timed
    public ResponseEntity<EdmsResponseDTO> updateEdmsResponse(@Valid @RequestBody EdmsResponseDTO edmsResponseDTO) throws URISyntaxException {
        log.debug("REST request to update EdmsResponse : {}", edmsResponseDTO);
        if (edmsResponseDTO.getId() == null) {
            return createEdmsResponse(edmsResponseDTO);
        }
        EdmsResponseDTO result = edmsResponseService.save(edmsResponseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("edmsResponse", edmsResponseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /edms-responses : get all the edmsResponses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of edmsResponses in body
     */
    @GetMapping("/edms-responses")
    @Timed
    public List<EdmsResponseDTO> getAllEdmsResponses() {
        log.debug("REST request to get all EdmsResponses");
        return edmsResponseService.findAll();
    }

    /**
     * GET  /edms-responses/:id : get the "id" edmsResponse.
     *
     * @param id the id of the edmsResponseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the edmsResponseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/edms-responses/{id}")
    @Timed
    public ResponseEntity<EdmsResponseDTO> getEdmsResponse(@PathVariable Long id) {
        log.debug("REST request to get EdmsResponse : {}", id);
        EdmsResponseDTO edmsResponseDTO = edmsResponseService.findOne(id);
        return Optional.ofNullable(edmsResponseDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /edms-responses/:id : delete the "id" edmsResponse.
     *
     * @param id the id of the edmsResponseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/edms-responses/{id}")
    @Timed
    public ResponseEntity<Void> deleteEdmsResponse(@PathVariable Long id) {
        log.debug("REST request to delete EdmsResponse : {}", id);
        edmsResponseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("edmsResponse", id.toString())).build();
    }

    /**
     * SEARCH  /_search/edms-responses?query=:query : search for the edmsResponse corresponding
     * to the query.
     *
     * @param query the query of the edmsResponse search 
     * @return the result of the search
     */
    @GetMapping("/_search/edms-responses")
    @Timed
    public List<EdmsResponseDTO> searchEdmsResponses(@RequestParam String query) {
        log.debug("REST request to search EdmsResponses for query {}", query);
        return edmsResponseService.search(query);
    }


}
