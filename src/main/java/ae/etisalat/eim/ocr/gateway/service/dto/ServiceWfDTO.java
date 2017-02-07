package ae.etisalat.eim.ocr.gateway.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ae.etisalat.eim.ocr.gateway.domain.enumeration.Status;
import ae.etisalat.eim.ocr.gateway.domain.enumeration.WfStatus;

/**
 * A DTO for the ServiceWf entity.
 */
public class ServiceWfDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Status status;

    @NotNull
    private WfStatus wfStatus;

    private String updatedBy;


    private Long serviceRespId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public WfStatus getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(WfStatus wfStatus) {
        this.wfStatus = wfStatus;
    }
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getServiceRespId() {
        return serviceRespId;
    }

    public void setServiceRespId(Long serviceRespId) {
        this.serviceRespId = serviceRespId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceWfDTO serviceWfDTO = (ServiceWfDTO) o;

        if ( ! Objects.equals(id, serviceWfDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceWfDTO{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", wfStatus='" + wfStatus + "'" +
            ", updatedBy='" + updatedBy + "'" +
            '}';
    }
}
