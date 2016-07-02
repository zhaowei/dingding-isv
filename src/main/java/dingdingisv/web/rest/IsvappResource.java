package dingdingisv.web.rest;

import com.codahale.metrics.annotation.Timed;
import dingdingisv.domain.Isvapp;
import dingdingisv.service.IsvappService;
import dingdingisv.web.rest.util.HeaderUtil;
import dingdingisv.web.rest.util.PaginationUtil;
import dingdingisv.web.rest.dto.IsvappDTO;
import dingdingisv.web.rest.mapper.IsvappMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Isvapp.
 */
@RestController
@RequestMapping("/api")
public class IsvappResource {

    private final Logger log = LoggerFactory.getLogger(IsvappResource.class);

    @Inject
    private IsvappService isvappService;

    @Inject
    private IsvappMapper isvappMapper;

    /**
     * POST  /isvapps : Create a new isvapp.
     *
     * @param isvappDTO the isvappDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new isvappDTO, or with status 400 (Bad Request) if the isvapp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/isvapps",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IsvappDTO> createIsvapp(@RequestBody IsvappDTO isvappDTO) throws URISyntaxException {
        log.debug("REST request to save Isvapp : {}", isvappDTO);
        if (isvappDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("isvapp", "idexists", "A new isvapp cannot already have an ID")).body(null);
        }
        IsvappDTO result = isvappService.save(isvappDTO);
        return ResponseEntity.created(new URI("/api/isvapps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("isvapp", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /isvapps : Updates an existing isvapp.
     *
     * @param isvappDTO the isvappDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated isvappDTO,
     * or with status 400 (Bad Request) if the isvappDTO is not valid,
     * or with status 500 (Internal Server Error) if the isvappDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/isvapps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IsvappDTO> updateIsvapp(@RequestBody IsvappDTO isvappDTO) throws URISyntaxException {
        log.debug("REST request to update Isvapp : {}", isvappDTO);
        if (isvappDTO.getId() == null) {
            return createIsvapp(isvappDTO);
        }
        IsvappDTO result = isvappService.save(isvappDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("isvapp", isvappDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /isvapps : get all the isvapps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of isvapps in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/isvapps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<IsvappDTO>> getAllIsvapps(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Isvapps");
        Page<Isvapp> page = isvappService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/isvapps");
        return new ResponseEntity<>(isvappMapper.isvappsToIsvappDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /isvapps/:id : get the "id" isvapp.
     *
     * @param id the id of the isvappDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the isvappDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/isvapps/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IsvappDTO> getIsvapp(@PathVariable Long id) {
        log.debug("REST request to get Isvapp : {}", id);
        IsvappDTO isvappDTO = isvappService.findOne(id);
        return Optional.ofNullable(isvappDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/isvapp/{isvKey}/byIsvKey",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IsvappDTO> getIsvappByIsvKey(@PathVariable String isvKey) {
        log.debug("REST request to get Isvapp by isvKey {}", isvKey);

        IsvappDTO isvappDTO = isvappService.findOneByIsvKey(isvKey);
        return Optional.ofNullable(isvappDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /isvapps/:id : delete the "id" isvapp.
     *
     * @param id the id of the isvappDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/isvapps/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIsvapp(@PathVariable Long id) {
        log.debug("REST request to delete Isvapp : {}", id);
        isvappService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("isvapp", id.toString())).build();
    }

    /**
     * SEARCH  /_search/isvapps?query=:query : search for the isvapp corresponding
     * to the query.
     *
     * @param query the query of the isvapp search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/isvapps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<IsvappDTO>> searchIsvapps(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Isvapps for query {}", query);
        Page<Isvapp> page = isvappService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/isvapps");
        return new ResponseEntity<>(isvappMapper.isvappsToIsvappDTOs(page.getContent()), headers, HttpStatus.OK);
    }


}
