package ae.etisalat.eim.ocr.gateway.service.mapper;

import ae.etisalat.eim.ocr.gateway.domain.*;
import ae.etisalat.eim.ocr.gateway.service.dto.EdmsLocationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity EdmsLocation and its DTO EdmsLocationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EdmsLocationMapper {

    @Mapping(source = "edmsResponse.id", target = "edmsResponseId")
    EdmsLocationDTO edmsLocationToEdmsLocationDTO(EdmsLocation edmsLocation);

    List<EdmsLocationDTO> edmsLocationsToEdmsLocationDTOs(List<EdmsLocation> edmsLocations);

    @Mapping(source = "edmsResponseId", target = "edmsResponse")
    @Mapping(target = "edmsDownloads", ignore = true)
    EdmsLocation edmsLocationDTOToEdmsLocation(EdmsLocationDTO edmsLocationDTO);

    List<EdmsLocation> edmsLocationDTOsToEdmsLocations(List<EdmsLocationDTO> edmsLocationDTOs);

    default EdmsResponse edmsResponseFromId(Long id) {
        if (id == null) {
            return null;
        }
        EdmsResponse edmsResponse = new EdmsResponse();
        edmsResponse.setId(id);
        return edmsResponse;
    }
}
