package ae.etisalat.eim.ocr.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import ae.etisalat.eim.ocr.gateway.service.ServiceRespService;
import ae.etisalat.eim.ocr.gateway.web.rest.util.HeaderUtil;
import ae.etisalat.eim.ocr.gateway.service.dto.ServiceRespDTO;

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
 * REST controller for managing ServiceResp.
 */
@RestController
@RequestMapping("/api")
public class ServiceRespResource {

    private final Logger log = LoggerFactory.getLogger(ServiceRespResource.class);
        
    @Inject
    private ServiceRespService serviceRespService;

    /**
     * POST  /service-resps : Create a new serviceResp.
     *
     * @param serviceRespDTO the serviceRespDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceRespDTO, or with status 400 (Bad Request) if the serviceResp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-resps")
    @Timed
    public ResponseEntity<ServiceRespDTO> createServiceResp(@Valid @RequestBody ServiceRespDTO serviceRespDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceResp : {}", serviceRespDTO);
        if (serviceRespDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceResp", "idexists", "A new serviceResp cannot already have an ID")).body(null);
        }
        ServiceRespDTO result = serviceRespService.save(serviceRespDTO);
        return ResponseEntity.created(new URI("/api/service-resps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceResp", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-resps : Updates an existing serviceResp.
     *
     * @param serviceRespDTO the serviceRespDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceRespDTO,
     * or with status 400 (Bad Request) if the serviceRespDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceRespDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/service-resps")
    @Timed
    public ResponseEntity<ServiceRespDTO> updateServiceResp(@Valid @RequestBody ServiceRespDTO serviceRespDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceResp : {}", serviceRespDTO);
        if (serviceRespDTO.getId() == null) {
            return createServiceResp(serviceRespDTO);
        }
        ServiceRespDTO result = serviceRespService.save(serviceRespDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceResp", serviceRespDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-resps : get all the serviceResps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of serviceResps in body
     */
    @GetMapping("/service-resps")
    @Timed
    public List<ServiceRespDTO> getAllServiceResps() {
        log.debug("REST request to get all ServiceResps");
        return serviceRespService.findAll();
    }

    /**
     * GET  /service-resps/:id : get the "id" serviceResp.
     *
     * @param id the id of the serviceRespDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceRespDTO, or with status 404 (Not Found)
     */
    @GetMapping("/service-resps/{id}")
    @Timed
    public ResponseEntity<ServiceRespDTO> getServiceResp(@PathVariable Long id) {
        log.debug("REST request to get ServiceResp : {}", id);
        ServiceRespDTO serviceRespDTO = serviceRespService.findOne(id);
        return Optional.ofNullable(serviceRespDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-resps/:id : delete the "id" serviceResp.
     *
     * @param id the id of the serviceRespDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-resps/{id}")
    @Timed
    public ResponseEntity<Void> deleteServiceResp(@PathVariable Long id) {
        log.debug("REST request to delete ServiceResp : {}", id);
        serviceRespService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceResp", id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-resps?query=:query : search for the serviceResp corresponding
     * to the query.
     *
     * @param query the query of the serviceResp search 
     * @return the result of the search
     */
    @GetMapping("/_search/service-resps")
    @Timed
    public List<ServiceRespDTO> searchServiceResps(@RequestParam String query) {
        log.debug("REST request to search ServiceResps for query {}", query);
        return serviceRespService.search(query);
    }


}
