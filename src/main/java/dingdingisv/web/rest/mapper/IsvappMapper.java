package dingdingisv.web.rest.mapper;

import dingdingisv.domain.*;
import dingdingisv.web.rest.dto.IsvappDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Isvapp and its DTO IsvappDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IsvappMapper {

    IsvappDTO isvappToIsvappDTO(Isvapp isvapp);

    List<IsvappDTO> isvappsToIsvappDTOs(List<Isvapp> isvapps);

    Isvapp isvappDTOToIsvapp(IsvappDTO isvappDTO);

    List<Isvapp> isvappDTOsToIsvapps(List<IsvappDTO> isvappDTOs);
}
