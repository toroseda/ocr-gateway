package ae.etisalat.eim.ocr.gateway.service.mapper;

import ae.etisalat.eim.ocr.gateway.domain.*;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsResponseDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity EdmsResponse and its DTO EdmsResponseDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EdmsResponseMapper {

    @Mapping(source = "edmsRequest.id", target = "edmsRequestId")
    EdmsResponseDTO edmsResponseToEdmsResponseDTO(EdmsResponse edmsResponse);

    List<EdmsResponseDTO> edmsResponsesToEdmsResponseDTOs(List<EdmsResponse> edmsResponses);

    @Mapping(source = "edmsRequestId", target = "edmsRequest")
    EdmsResponse edmsResponseDTOToEdmsResponse(EdmsResponseDTO edmsResponseDTO);

    List<EdmsResponse> edmsResponseDTOsToEdmsResponses(List<EdmsResponseDTO> edmsResponseDTOs);

    default EdmsRequest edmsRequestFromId(Long id) {
        if (id == null) {
            return null;
        }
        EdmsRequest edmsRequest = new EdmsRequest();
        edmsRequest.setId(id);
        return edmsRequest;
    }
}
