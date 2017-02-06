package ae.etisalat.eim.ocr.gateway.service.mapper;

import ae.etisalat.eim.ocr.gateway.domain.*;
import ae.etisalat.eim.ocr.gateway.service.dto.SessionWfDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SessionWf and its DTO SessionWfDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SessionWfMapper {

    @Mapping(source = "ocrSession.id", target = "ocrSessionId")
    SessionWfDTO sessionWfToSessionWfDTO(SessionWf sessionWf);

    List<SessionWfDTO> sessionWfsToSessionWfDTOs(List<SessionWf> sessionWfs);

    @Mapping(source = "ocrSessionId", target = "ocrSession")
    SessionWf sessionWfDTOToSessionWf(SessionWfDTO sessionWfDTO);

    List<SessionWf> sessionWfDTOsToSessionWfs(List<SessionWfDTO> sessionWfDTOs);

    default OcrSession ocrSessionFromId(Long id) {
        if (id == null) {
            return null;
        }
        OcrSession ocrSession = new OcrSession();
        ocrSession.setId(id);
        return ocrSession;
    }
}
