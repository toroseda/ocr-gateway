package ae.etisalat.eim.ocr.gateway.service.mapper;

import ae.etisalat.eim.ocr.gateway.domain.*;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsRequestDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity EdmsRequest and its DTO EdmsRequestDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EdmsRequestMapper {

    @Mapping(source = "ocrSession.id", target = "ocrSessionId")
    EdmsRequestDTO edmsRequestToEdmsRequestDTO(EdmsRequest edmsRequest);

    List<EdmsRequestDTO> edmsRequestsToEdmsRequestDTOs(List<EdmsRequest> edmsRequests);

    @Mapping(source = "ocrSessionId", target = "ocrSession")
    @Mapping(target = "requestWfs", ignore = true)
    EdmsRequest edmsRequestDTOToEdmsRequest(EdmsRequestDTO edmsRequestDTO);

    List<EdmsRequest> edmsRequestDTOsToEdmsRequests(List<EdmsRequestDTO> edmsRequestDTOs);

    default OcrSession ocrSessionFromId(Long id) {
        if (id == null) {
            return null;
        }
        OcrSession ocrSession = new OcrSession();
        ocrSession.setId(id);
        return ocrSession;
    }
}
