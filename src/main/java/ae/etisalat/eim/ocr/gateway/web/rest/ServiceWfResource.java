package ae.etisalat.eim.ocr.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import ae.etisalat.eim.ocr.gateway.service.ServiceWfService;
import ae.etisalat.eim.ocr.gateway.web.rest.util.HeaderUtil;
import ae.etisalat.eim.ocr.gateway.service.dto.ServiceWfDTO;

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
 * REST controller for managing ServiceWf.
 */
@RestController
@RequestMapping("/api")
public class ServiceWfResource {

    private final Logger log = LoggerFactory.getLogger(ServiceWfResource.class);
        
    @Inject
    private ServiceWfService serviceWfService;

    /**
     * POST  /service-wfs : Create a new serviceWf.
     *
     * @param serviceWfDTO the serviceWfDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceWfDTO, or with status 400 (Bad Request) if the serviceWf has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-wfs")
    @Timed
    public ResponseEntity<ServiceWfDTO> createServiceWf(@Valid @RequestBody ServiceWfDTO serviceWfDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceWf : {}", serviceWfDTO);
        if (serviceWfDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceWf", "idexists", "A new serviceWf cannot already have an ID")).body(null);
        }
        ServiceWfDTO result = serviceWfService.save(serviceWfDTO);
        return ResponseEntity.created(new URI("/api/service-wfs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceWf", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-wfs : Updates an existing serviceWf.
     *
     * @param serviceWfDTO the serviceWfDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceWfDTO,
     * or with status 400 (Bad Request) if the serviceWfDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceWfDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/service-wfs")
    @Timed
    public ResponseEntity<ServiceWfDTO> updateServiceWf(@Valid @RequestBody ServiceWfDTO serviceWfDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceWf : {}", serviceWfDTO);
        if (serviceWfDTO.getId() == null) {
            return createServiceWf(serviceWfDTO);
        }
        ServiceWfDTO result = serviceWfService.save(serviceWfDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceWf", serviceWfDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-wfs : get all the serviceWfs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of serviceWfs in body
     */
    @GetMapping("/service-wfs")
    @Timed
    public List<ServiceWfDTO> getAllServiceWfs() {
        log.debug("REST request to get all ServiceWfs");
        return serviceWfService.findAll();
    }

    /**
     * GET  /service-wfs/:id : get the "id" serviceWf.
     *
     * @param id the id of the serviceWfDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceWfDTO, or with status 404 (Not Found)
     */
    @GetMapping("/service-wfs/{id}")
    @Timed
    public ResponseEntity<ServiceWfDTO> getServiceWf(@PathVariable Long id) {
        log.debug("REST request to get ServiceWf : {}", id);
        ServiceWfDTO serviceWfDTO = serviceWfService.findOne(id);
        return Optional.ofNullable(serviceWfDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-wfs/:id : delete the "id" serviceWf.
     *
     * @param id the id of the serviceWfDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-wfs/{id}")
    @Timed
    public ResponseEntity<Void> deleteServiceWf(@PathVariable Long id) {
        log.debug("REST request to delete ServiceWf : {}", id);
        serviceWfService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceWf", id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-wfs?query=:query : search for the serviceWf corresponding
     * to the query.
     *
     * @param query the query of the serviceWf search 
     * @return the result of the search
     */
    @GetMapping("/_search/service-wfs")
    @Timed
    public List<ServiceWfDTO> searchServiceWfs(@RequestParam String query) {
        log.debug("REST request to search ServiceWfs for query {}", query);
        return serviceWfService.search(query);
    }


}
