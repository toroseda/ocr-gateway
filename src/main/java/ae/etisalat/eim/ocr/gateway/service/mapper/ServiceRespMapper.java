package ae.etisalat.eim.ocr.gateway.service.mapper;

import ae.etisalat.eim.ocr.gateway.domain.*;
import ae.etisalat.eim.ocr.gateway.service.dto.ServiceRespDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ServiceResp and its DTO ServiceRespDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceRespMapper {

    @Mapping(source = "edmsDownload.id", target = "edmsDownloadId")
    ServiceRespDTO serviceRespToServiceRespDTO(ServiceResp serviceResp);

    List<ServiceRespDTO> serviceRespsToServiceRespDTOs(List<ServiceResp> serviceResps);

    @Mapping(source = "edmsDownloadId", target = "edmsDownload")
    @Mapping(target = "serviceWfs", ignore = true)
    ServiceResp serviceRespDTOToServiceResp(ServiceRespDTO serviceRespDTO);

    List<ServiceResp> serviceRespDTOsToServiceResps(List<ServiceRespDTO> serviceRespDTOs);

    default EdmsDownload edmsDownloadFromId(Long id) {
        if (id == null) {
            return null;
        }
        EdmsDownload edmsDownload = new EdmsDownload();
        edmsDownload.setId(id);
        return edmsDownload;
    }
}
