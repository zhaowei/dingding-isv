package dingdingisv.repository;

import dingdingisv.domain.IsvappPermantCode;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the IsvappPermantCode entity.
 */
@SuppressWarnings("unused")
public interface IsvappPermantCodeRepository extends JpaRepository<IsvappPermantCode,Long> {

    void deleteByIsvFidAndCorpId(Integer isvFid, String corpId);
    IsvappPermantCode findOneByIsvFidAndCorpId(Integer isvFid, String corpId);
}
