package dingdingisv.web.rest;

import com.codahale.metrics.annotation.Timed;
import dingdingisv.domain.IsvappPermantCode;
import dingdingisv.service.IsvappPermantCodeService;
import dingdingisv.web.rest.util.HeaderUtil;
import dingdingisv.web.rest.util.PaginationUtil;
import dingdingisv.web.rest.dto.IsvappPermantCodeDTO;
import dingdingisv.web.rest.mapper.IsvappPermantCodeMapper;
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
 * REST controller for managing IsvappPermantCode.
 */
@RestController
@RequestMapping("/api")
public class IsvappPermantCodeResource {

    private final Logger log = LoggerFactory.getLogger(IsvappPermantCodeResource.class);
        
    @Inject
    private IsvappPermantCodeService isvappPermantCodeService;
    
    @Inject
    private IsvappPermantCodeMapper isvappPermantCodeMapper;
    
    /**
     * POST  /isvapp-permant-codes : Create a new isvappPermantCode.
     *
     * @param isvappPermantCodeDTO the isvappPermantCodeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new isvappPermantCodeDTO, or with status 400 (Bad Request) if the isvappPermantCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/isvapp-permant-codes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IsvappPermantCodeDTO> createIsvappPermantCode(@RequestBody IsvappPermantCodeDTO isvappPermantCodeDTO) throws URISyntaxException {
        log.debug("REST request to save IsvappPermantCode : {}", isvappPermantCodeDTO);
        if (isvappPermantCodeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("isvappPermantCode", "idexists", "A new isvappPermantCode cannot already have an ID")).body(null);
        }
        IsvappPermantCodeDTO result = isvappPermantCodeService.save(isvappPermantCodeDTO);
        return ResponseEntity.created(new URI("/api/isvapp-permant-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("isvappPermantCode", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /isvapp-permant-codes : Updates an existing isvappPermantCode.
     *
     * @param isvappPermantCodeDTO the isvappPermantCodeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated isvappPermantCodeDTO,
     * or with status 400 (Bad Request) if the isvappPermantCodeDTO is not valid,
     * or with status 500 (Internal Server Error) if the isvappPermantCodeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/isvapp-permant-codes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IsvappPermantCodeDTO> updateIsvappPermantCode(@RequestBody IsvappPermantCodeDTO isvappPermantCodeDTO) throws URISyntaxException {
        log.debug("REST request to update IsvappPermantCode : {}", isvappPermantCodeDTO);
        if (isvappPermantCodeDTO.getId() == null) {
            return createIsvappPermantCode(isvappPermantCodeDTO);
        }
        IsvappPermantCodeDTO result = isvappPermantCodeService.save(isvappPermantCodeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("isvappPermantCode", isvappPermantCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /isvapp-permant-codes : get all the isvappPermantCodes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of isvappPermantCodes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/isvapp-permant-codes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<IsvappPermantCodeDTO>> getAllIsvappPermantCodes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of IsvappPermantCodes");
        Page<IsvappPermantCode> page = isvappPermantCodeService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/isvapp-permant-codes");
        return new ResponseEntity<>(isvappPermantCodeMapper.isvappPermantCodesToIsvappPermantCodeDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /isvapp-permant-codes/:id : get the "id" isvappPermantCode.
     *
     * @param id the id of the isvappPermantCodeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the isvappPermantCodeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/isvapp-permant-codes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IsvappPermantCodeDTO> getIsvappPermantCode(@PathVariable Long id) {
        log.debug("REST request to get IsvappPermantCode : {}", id);
        IsvappPermantCodeDTO isvappPermantCodeDTO = isvappPermantCodeService.findOne(id);
        return Optional.ofNullable(isvappPermantCodeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /isvapp-permant-codes/:id : delete the "id" isvappPermantCode.
     *
     * @param id the id of the isvappPermantCodeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/isvapp-permant-codes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIsvappPermantCode(@PathVariable Long id) {
        log.debug("REST request to delete IsvappPermantCode : {}", id);
        isvappPermantCodeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("isvappPermantCode", id.toString())).build();
    }

    /**
     * SEARCH  /_search/isvapp-permant-codes?query=:query : search for the isvappPermantCode corresponding
     * to the query.
     *
     * @param query the query of the isvappPermantCode search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/isvapp-permant-codes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<IsvappPermantCodeDTO>> searchIsvappPermantCodes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of IsvappPermantCodes for query {}", query);
        Page<IsvappPermantCode> page = isvappPermantCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/isvapp-permant-codes");
        return new ResponseEntity<>(isvappPermantCodeMapper.isvappPermantCodesToIsvappPermantCodeDTOs(page.getContent()), headers, HttpStatus.OK);
    }


}
