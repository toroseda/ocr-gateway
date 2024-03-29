package ae.etisalat.eim.ocr.gateway.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ae.etisalat.eim.ocr.gateway.domain.enumeration.Status;

/**
 * A DTO for the EdmsLocation entity.
 */
public class EdmsLocationDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String actualDirectory;

    @NotNull
    private Status status;


    private Long edmsResponseId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getActualDirectory() {
        return actualDirectory;
    }

    public void setActualDirectory(String actualDirectory) {
        this.actualDirectory = actualDirectory;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getEdmsResponseId() {
        return edmsResponseId;
    }

    public void setEdmsResponseId(Long edmsResponseId) {
        this.edmsResponseId = edmsResponseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EdmsLocationDTO edmsLocationDTO = (EdmsLocationDTO) o;

        if ( ! Objects.equals(id, edmsLocationDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EdmsLocationDTO{" +
            "id=" + id +
            ", actualDirectory='" + actualDirectory + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
