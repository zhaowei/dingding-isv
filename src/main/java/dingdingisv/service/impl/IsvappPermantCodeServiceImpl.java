package dingdingisv.service.impl;

import dingdingisv.service.IsvappPermantCodeService;
import dingdingisv.domain.IsvappPermantCode;
import dingdingisv.repository.IsvappPermantCodeRepository;
import dingdingisv.repository.search.IsvappPermantCodeSearchRepository;
import dingdingisv.web.rest.dto.IsvappPermantCodeDTO;
import dingdingisv.web.rest.mapper.IsvappPermantCodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing IsvappPermantCode.
 */
@Service
@Transactional
public class IsvappPermantCodeServiceImpl implements IsvappPermantCodeService{

    private final Logger log = LoggerFactory.getLogger(IsvappPermantCodeServiceImpl.class);

    @Inject
    private IsvappPermantCodeRepository isvappPermantCodeRepository;

    @Inject
    private IsvappPermantCodeMapper isvappPermantCodeMapper;

    @Inject
    private IsvappPermantCodeSearchRepository isvappPermantCodeSearchRepository;

    @PersistenceContext
    private EntityManager em;

    private final ZonedDateTime UPDATED_UPDATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    /**
     * Save a isvappPermantCode.
     *
     * @param isvappPermantCodeDTO the entity to save
     * @return the persisted entity
     */
    public IsvappPermantCodeDTO save(IsvappPermantCodeDTO isvappPermantCodeDTO) {
        log.debug("Request to save IsvappPermantCode : {}", isvappPermantCodeDTO);
        if (isvappPermantCodeDTO.getId() != null) {
            isvappPermantCodeDTO.setUpdateTime(UPDATED_UPDATE_TIME);
        } else {
            isvappPermantCodeDTO.setCreateTime(UPDATED_CREATE_TIME);
            isvappPermantCodeDTO.setUpdateTime(UPDATED_UPDATE_TIME);
        }
        IsvappPermantCode isvappPermantCode = isvappPermantCodeMapper.isvappPermantCodeDTOToIsvappPermantCode(isvappPermantCodeDTO);
        isvappPermantCode = isvappPermantCodeRepository.save(isvappPermantCode);
        IsvappPermantCodeDTO result = isvappPermantCodeMapper.isvappPermantCodeToIsvappPermantCodeDTO(isvappPermantCode);
        isvappPermantCodeSearchRepository.save(isvappPermantCode);
        return result;
    }

    /**
     *  Get all the isvappPermantCodes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IsvappPermantCode> findAll(Pageable pageable) {
        log.debug("Request to get all IsvappPermantCodes");
        Page<IsvappPermantCode> result = isvappPermantCodeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one isvappPermantCode by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public IsvappPermantCodeDTO findOne(Long id) {
        log.debug("Request to get IsvappPermantCode : {}", id);
        IsvappPermantCode isvappPermantCode = isvappPermantCodeRepository.findOne(id);
        IsvappPermantCodeDTO isvappPermantCodeDTO = isvappPermantCodeMapper.isvappPermantCodeToIsvappPermantCodeDTO(isvappPermantCode);
        return isvappPermantCodeDTO;
    }

    public IsvappPermantCodeDTO findOneByIsvFidAndCorpId(Integer isvFid, String corpId) {
        log.debug("Requst to get IsvappPermantCode by findAndCorpId");

        TypedQuery query = em.createQuery("select a from IsvappPermantCode a where a.isvFid = ?1 and a.corpId=?2", IsvappPermantCode.class);
        query.setParameter(1, isvFid);
        query.setParameter(2, corpId);

        query.setMaxResults(1);
        IsvappPermantCode isvappPermantCode =  (IsvappPermantCode)query.getSingleResult();
        IsvappPermantCodeDTO isvappPermantCodeDTO = isvappPermantCodeMapper.isvappPermantCodeToIsvappPermantCodeDTO(isvappPermantCode);
        return isvappPermantCodeDTO;
    }

    public void deleteByIsvFidAndCorpId(Integer isvFid, String corpId) {
        isvappPermantCodeRepository.deleteByIsvFidAndCorpId(isvFid, corpId);
    }

    /**
     *  Delete the  isvappPermantCode by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete IsvappPermantCode : {}", id);
        isvappPermantCodeRepository.delete(id);
        isvappPermantCodeSearchRepository.delete(id);
    }

    /**
     * Search for the isvappPermantCode corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IsvappPermantCode> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IsvappPermantCodes for query {}", query);
        return isvappPermantCodeSearchRepository.search(queryStringQuery(query), pageable);
    }
}
