package ae.etisalat.eim.ocr.gateway.service.mapper;

import ae.etisalat.eim.ocr.gateway.domain.*;
import ae.etisalat.eim.ocr.gateway.service.dto.RequestWfDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity RequestWf and its DTO RequestWfDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RequestWfMapper {

    @Mapping(source = "edmsRequest.id", target = "edmsRequestId")
    RequestWfDTO requestWfToRequestWfDTO(RequestWf requestWf);

    List<RequestWfDTO> requestWfsToRequestWfDTOs(List<RequestWf> requestWfs);

    @Mapping(source = "edmsRequestId", target = "edmsRequest")
    RequestWf requestWfDTOToRequestWf(RequestWfDTO requestWfDTO);

    List<RequestWf> requestWfDTOsToRequestWfs(List<RequestWfDTO> requestWfDTOs);

    default EdmsRequest edmsRequestFromId(Long id) {
        if (id == null) {
            return null;
        }
        EdmsRequest edmsRequest = new EdmsRequest();
        edmsRequest.setId(id);
        return edmsRequest;
    }
}
