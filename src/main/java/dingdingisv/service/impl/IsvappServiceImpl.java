package dingdingisv.service.impl;

import dingdingisv.service.IsvappService;
import dingdingisv.domain.Isvapp;
import dingdingisv.repository.IsvappRepository;
import dingdingisv.repository.search.IsvappSearchRepository;
import dingdingisv.web.rest.dto.IsvappDTO;
import dingdingisv.web.rest.mapper.IsvappMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Isvapp.
 */
@Service
@Transactional
public class IsvappServiceImpl implements IsvappService{

    private final Logger log = LoggerFactory.getLogger(IsvappServiceImpl.class);

    @Inject
    private IsvappRepository isvappRepository;

    @Inject
    private IsvappMapper isvappMapper;

    @Inject
    private IsvappSearchRepository isvappSearchRepository;

    private static final ZonedDateTime UPDATED_UPDATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);


    /**
     * Save a isvapp.
     *
     * @param isvappDTO the entity to save
     * @return the persisted entity
     */
    public IsvappDTO save(IsvappDTO isvappDTO) {
        log.debug("Request to save Isvapp : {}", isvappDTO);
        if (isvappDTO.getId() != null) {
            isvappDTO.setUpdateTime(UPDATED_UPDATE_TIME);
        } else {
            isvappDTO.setCreateTime(UPDATED_CREATE_TIME);
            isvappDTO.setUpdateTime(UPDATED_UPDATE_TIME);
        }
        Isvapp isvapp = isvappMapper.isvappDTOToIsvapp(isvappDTO);
        isvapp = isvappRepository.save(isvapp);
        IsvappDTO result = isvappMapper.isvappToIsvappDTO(isvapp);
        isvappSearchRepository.save(isvapp);
        return result;
    }

    /**
     *  Get all the isvapps.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Isvapp> findAll(Pageable pageable) {
        log.debug("Request to get all Isvapps");
        Page<Isvapp> result = isvappRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one isvapp by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public IsvappDTO findOne(Long id) {
        log.debug("Request to get Isvapp : {}", id);
        Isvapp isvapp = isvappRepository.findOne(id);
        IsvappDTO isvappDTO = isvappMapper.isvappToIsvappDTO(isvapp);
        return isvappDTO;
    }

    public  IsvappDTO findOneByIsvKey(String isvKey) {
        log.debug("Request to get Isvapp by isvKey: {}", isvKey);
        Isvapp isvapp = isvappRepository.findOneByIsvKey(isvKey);
        IsvappDTO isvappDTO = isvappMapper.isvappToIsvappDTO(isvapp);
        return isvappDTO;
    }

    /**
     *  Delete the  isvapp by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Isvapp : {}", id);
        isvappRepository.delete(id);
        isvappSearchRepository.delete(id);
    }

    /**
     * Search for the isvapp corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Isvapp> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Isvapps for query {}", query);
        return isvappSearchRepository.search(queryStringQuery(query), pageable);
    }
}
