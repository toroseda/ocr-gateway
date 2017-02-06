package ae.etisalat.eim.ocr.gateway.service.mapper;

import ae.etisalat.eim.ocr.gateway.domain.*;
import ae.etisalat.eim.ocr.gateway.service.dto.ServiceWfDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ServiceWf and its DTO ServiceWfDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceWfMapper {

    @Mapping(source = "serviceResp.id", target = "serviceRespId")
    ServiceWfDTO serviceWfToServiceWfDTO(ServiceWf serviceWf);

    List<ServiceWfDTO> serviceWfsToServiceWfDTOs(List<ServiceWf> serviceWfs);

    @Mapping(source = "serviceRespId", target = "serviceResp")
    ServiceWf serviceWfDTOToServiceWf(ServiceWfDTO serviceWfDTO);

    List<ServiceWf> serviceWfDTOsToServiceWfs(List<ServiceWfDTO> serviceWfDTOs);

    default ServiceResp serviceRespFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceResp serviceResp = new ServiceResp();
        serviceResp.setId(id);
        return serviceResp;
    }
}
