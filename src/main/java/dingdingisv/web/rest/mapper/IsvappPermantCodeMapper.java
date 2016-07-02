package dingdingisv.web.rest.mapper;

import dingdingisv.domain.*;
import dingdingisv.web.rest.dto.IsvappPermantCodeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity IsvappPermantCode and its DTO IsvappPermantCodeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IsvappPermantCodeMapper {

    IsvappPermantCodeDTO isvappPermantCodeToIsvappPermantCodeDTO(IsvappPermantCode isvappPermantCode);

    List<IsvappPermantCodeDTO> isvappPermantCodesToIsvappPermantCodeDTOs(List<IsvappPermantCode> isvappPermantCodes);

    IsvappPermantCode isvappPermantCodeDTOToIsvappPermantCode(IsvappPermantCodeDTO isvappPermantCodeDTO);

    List<IsvappPermantCode> isvappPermantCodeDTOsToIsvappPermantCodes(List<IsvappPermantCodeDTO> isvappPermantCodeDTOs);
}
