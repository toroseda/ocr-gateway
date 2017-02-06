package ae.etisalat.eim.ocr.gateway.service.mapper;

import ae.etisalat.eim.ocr.gateway.domain.*;
import ae.etisalat.eim.ocr.gateway.service.dto.OcrSessionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity OcrSession and its DTO OcrSessionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OcrSessionMapper {

    OcrSessionDTO ocrSessionToOcrSessionDTO(OcrSession ocrSession);

    List<OcrSessionDTO> ocrSessionsToOcrSessionDTOs(List<OcrSession> ocrSessions);

    @Mapping(target = "sessionWfs", ignore = true)
    @Mapping(target = "edmsRequests", ignore = true)
    OcrSession ocrSessionDTOToOcrSession(OcrSessionDTO ocrSessionDTO);

    List<OcrSession> ocrSessionDTOsToOcrSessions(List<OcrSessionDTO> ocrSessionDTOs);
}
