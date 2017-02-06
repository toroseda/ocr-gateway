package ae.etisalat.eim.ocr.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import ae.etisalat.eim.ocr.gateway.service.EdmsLocationService;
import ae.etisalat.eim.ocr.gateway.web.rest.util.HeaderUtil;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsLocationDTO;

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
 * REST controller for managing EdmsLocation.
 */
@RestController
@RequestMapping("/api")
public class EdmsLocationResource {

    private final Logger log = LoggerFactory.getLogger(EdmsLocationResource.class);
        
    @Inject
    private EdmsLocationService edmsLocationService;

    /**
     * POST  /edms-locations : Create a new edmsLocation.
     *
     * @param edmsLocationDTO the edmsLocationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new edmsLocationDTO, or with status 400 (Bad Request) if the edmsLocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/edms-locations")
    @Timed
    public ResponseEntity<EdmsLocationDTO> createEdmsLocation(@Valid @RequestBody EdmsLocationDTO edmsLocationDTO) throws URISyntaxException {
        log.debug("REST request to save EdmsLocation : {}", edmsLocationDTO);
        if (edmsLocationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("edmsLocation", "idexists", "A new edmsLocation cannot already have an ID")).body(null);
        }
        EdmsLocationDTO result = edmsLocationService.save(edmsLocationDTO);
        return ResponseEntity.created(new URI("/api/edms-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("edmsLocation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /edms-locations : Updates an existing edmsLocation.
     *
     * @param edmsLocationDTO the edmsLocationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated edmsLocationDTO,
     * or with status 400 (Bad Request) if the edmsLocationDTO is not valid,
     * or with status 500 (Internal Server Error) if the edmsLocationDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/edms-locations")
    @Timed
    public ResponseEntity<EdmsLocationDTO> updateEdmsLocation(@Valid @RequestBody EdmsLocationDTO edmsLocationDTO) throws URISyntaxException {
        log.debug("REST request to update EdmsLocation : {}", edmsLocationDTO);
        if (edmsLocationDTO.getId() == null) {
            return createEdmsLocation(edmsLocationDTO);
        }
        EdmsLocationDTO result = edmsLocationService.save(edmsLocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("edmsLocation", edmsLocationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /edms-locations : get all the edmsLocations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of edmsLocations in body
     */
    @GetMapping("/edms-locations")
    @Timed
    public List<EdmsLocationDTO> getAllEdmsLocations() {
        log.debug("REST request to get all EdmsLocations");
        return edmsLocationService.findAll();
    }

    /**
     * GET  /edms-locations/:id : get the "id" edmsLocation.
     *
     * @param id the id of the edmsLocationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the edmsLocationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/edms-locations/{id}")
    @Timed
    public ResponseEntity<EdmsLocationDTO> getEdmsLocation(@PathVariable Long id) {
        log.debug("REST request to get EdmsLocation : {}", id);
        EdmsLocationDTO edmsLocationDTO = edmsLocationService.findOne(id);
        return Optional.ofNullable(edmsLocationDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /edms-locations/:id : delete the "id" edmsLocation.
     *
     * @param id the id of the edmsLocationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/edms-locations/{id}")
    @Timed
    public ResponseEntity<Void> deleteEdmsLocation(@PathVariable Long id) {
        log.debug("REST request to delete EdmsLocation : {}", id);
        edmsLocationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("edmsLocation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/edms-locations?query=:query : search for the edmsLocation corresponding
     * to the query.
     *
     * @param query the query of the edmsLocation search 
     * @return the result of the search
     */
    @GetMapping("/_search/edms-locations")
    @Timed
    public List<EdmsLocationDTO> searchEdmsLocations(@RequestParam String query) {
        log.debug("REST request to search EdmsLocations for query {}", query);
        return edmsLocationService.search(query);
    }


}
