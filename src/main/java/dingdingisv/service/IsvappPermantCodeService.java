package dingdingisv.service;

import dingdingisv.domain.IsvappPermantCode;
import dingdingisv.web.rest.dto.IsvappPermantCodeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing IsvappPermantCode.
 */
public interface IsvappPermantCodeService {

    /**
     * Save a isvappPermantCode.
     *
     * @param isvappPermantCodeDTO the entity to save
     * @return the persisted entity
     */
    IsvappPermantCodeDTO save(IsvappPermantCodeDTO isvappPermantCodeDTO);

    /**
     *  Get all the isvappPermantCodes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<IsvappPermantCode> findAll(Pageable pageable);

    /**
     *  Get the "id" isvappPermantCode.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    IsvappPermantCodeDTO findOne(Long id);

    /**
     *  Delete the "id" isvappPermantCode.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the isvappPermantCode corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<IsvappPermantCode> search(String query, Pageable pageable);

    IsvappPermantCodeDTO findOneByIsvFidAndCorpId(Integer isvFid, String corpId);

    void deleteByIsvFidAndCorpId(Integer isvFid, String corpId);
}
