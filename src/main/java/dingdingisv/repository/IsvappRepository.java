package dingdingisv.repository;

import dingdingisv.domain.Isvapp;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Isvapp entity.
 */
@SuppressWarnings("unused")
public interface IsvappRepository extends JpaRepository<Isvapp,Long> {

    Isvapp findOneByIsvKey(String isvKey);

}
