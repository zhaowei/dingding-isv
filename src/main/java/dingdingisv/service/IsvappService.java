package dingdingisv.service;

import dingdingisv.domain.Isvapp;
import dingdingisv.web.rest.dto.IsvappDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Isvapp.
 */
public interface IsvappService {

    /**
     * Save a isvapp.
     *
     * @param isvappDTO the entity to save
     * @return the persisted entity
     */
    IsvappDTO save(IsvappDTO isvappDTO);

    /**
     *  Get all the isvapps.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Isvapp> findAll(Pageable pageable);

    /**
     *  Get the "id" isvapp.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    IsvappDTO findOne(Long id);

    IsvappDTO findOneByIsvKey(String isvKey);

    /**
     *  Delete the "id" isvapp.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the isvapp corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Isvapp> search(String query, Pageable pageable);
}
