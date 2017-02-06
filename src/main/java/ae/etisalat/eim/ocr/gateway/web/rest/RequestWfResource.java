package ae.etisalat.eim.ocr.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import ae.etisalat.eim.ocr.gateway.service.RequestWfService;
import ae.etisalat.eim.ocr.gateway.web.rest.util.HeaderUtil;
import ae.etisalat.eim.ocr.gateway.service.dto.RequestWfDTO;

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
 * REST controller for managing RequestWf.
 */
@RestController
@RequestMapping("/api")
public class RequestWfResource {

    private final Logger log = LoggerFactory.getLogger(RequestWfResource.class);
        
    @Inject
    private RequestWfService requestWfService;

    /**
     * POST  /request-wfs : Create a new requestWf.
     *
     * @param requestWfDTO the requestWfDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new requestWfDTO, or with status 400 (Bad Request) if the requestWf has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/request-wfs")
    @Timed
    public ResponseEntity<RequestWfDTO> createRequestWf(@Valid @RequestBody RequestWfDTO requestWfDTO) throws URISyntaxException {
        log.debug("REST request to save RequestWf : {}", requestWfDTO);
        if (requestWfDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("requestWf", "idexists", "A new requestWf cannot already have an ID")).body(null);
        }
        RequestWfDTO result = requestWfService.save(requestWfDTO);
        return ResponseEntity.created(new URI("/api/request-wfs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("requestWf", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /request-wfs : Updates an existing requestWf.
     *
     * @param requestWfDTO the requestWfDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated requestWfDTO,
     * or with status 400 (Bad Request) if the requestWfDTO is not valid,
     * or with status 500 (Internal Server Error) if the requestWfDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/request-wfs")
    @Timed
    public ResponseEntity<RequestWfDTO> updateRequestWf(@Valid @RequestBody RequestWfDTO requestWfDTO) throws URISyntaxException {
        log.debug("REST request to update RequestWf : {}", requestWfDTO);
        if (requestWfDTO.getId() == null) {
            return createRequestWf(requestWfDTO);
        }
        RequestWfDTO result = requestWfService.save(requestWfDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("requestWf", requestWfDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /request-wfs : get all the requestWfs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of requestWfs in body
     */
    @GetMapping("/request-wfs")
    @Timed
    public List<RequestWfDTO> getAllRequestWfs() {
        log.debug("REST request to get all RequestWfs");
        return requestWfService.findAll();
    }

    /**
     * GET  /request-wfs/:id : get the "id" requestWf.
     *
     * @param id the id of the requestWfDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the requestWfDTO, or with status 404 (Not Found)
     */
    @GetMapping("/request-wfs/{id}")
    @Timed
    public ResponseEntity<RequestWfDTO> getRequestWf(@PathVariable Long id) {
        log.debug("REST request to get RequestWf : {}", id);
        RequestWfDTO requestWfDTO = requestWfService.findOne(id);
        return Optional.ofNullable(requestWfDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /request-wfs/:id : delete the "id" requestWf.
     *
     * @param id the id of the requestWfDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/request-wfs/{id}")
    @Timed
    public ResponseEntity<Void> deleteRequestWf(@PathVariable Long id) {
        log.debug("REST request to delete RequestWf : {}", id);
        requestWfService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("requestWf", id.toString())).build();
    }

    /**
     * SEARCH  /_search/request-wfs?query=:query : search for the requestWf corresponding
     * to the query.
     *
     * @param query the query of the requestWf search 
     * @return the result of the search
     */
    @GetMapping("/_search/request-wfs")
    @Timed
    public List<RequestWfDTO> searchRequestWfs(@RequestParam String query) {
        log.debug("REST request to search RequestWfs for query {}", query);
        return requestWfService.search(query);
    }


}
