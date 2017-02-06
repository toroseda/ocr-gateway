package ae.etisalat.eim.ocr.gateway.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the SessionWf entity.
 */
public class SessionWfDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer statusId;

    @NotNull
    private Integer wfTypeId;

    private String updatedBy;


    private Long ocrSessionId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    public Integer getWfTypeId() {
        return wfTypeId;
    }

    public void setWfTypeId(Integer wfTypeId) {
        this.wfTypeId = wfTypeId;
    }
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getOcrSessionId() {
        return ocrSessionId;
    }

    public void setOcrSessionId(Long ocrSessionId) {
        this.ocrSessionId = ocrSessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SessionWfDTO sessionWfDTO = (SessionWfDTO) o;

        if ( ! Objects.equals(id, sessionWfDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SessionWfDTO{" +
            "id=" + id +
            ", statusId='" + statusId + "'" +
            ", wfTypeId='" + wfTypeId + "'" +
            ", updatedBy='" + updatedBy + "'" +
            '}';
    }
}
