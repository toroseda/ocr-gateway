package ae.etisalat.eim.ocr.gateway.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the EdmsDownload entity.
 */
public class EdmsDownloadDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String actualDirectory;

    @NotNull
    private String actualFilename;


    private Long edmsLocationId;
    
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
    public String getActualFilename() {
        return actualFilename;
    }

    public void setActualFilename(String actualFilename) {
        this.actualFilename = actualFilename;
    }

    public Long getEdmsLocationId() {
        return edmsLocationId;
    }

    public void setEdmsLocationId(Long edmsLocationId) {
        this.edmsLocationId = edmsLocationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EdmsDownloadDTO edmsDownloadDTO = (EdmsDownloadDTO) o;

        if ( ! Objects.equals(id, edmsDownloadDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EdmsDownloadDTO{" +
            "id=" + id +
            ", actualDirectory='" + actualDirectory + "'" +
            ", actualFilename='" + actualFilename + "'" +
            '}';
    }
}
