package ae.etisalat.eim.ocr.gateway.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;


/**
 * A DTO for the ServiceResp entity.
 */
public class ServiceRespDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Lob
    private byte[] rawJson;

    private String rawJsonContentType;
    private String documentImage;

    private LocalDate startDate;

    private LocalDate endDate;

    private String lastRunBy;

    private Integer lastRunDur;

    private LocalDate lastRunDate;


    private Long edmsDownloadId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public byte[] getRawJson() {
        return rawJson;
    }

    public void setRawJson(byte[] rawJson) {
        this.rawJson = rawJson;
    }

    public String getRawJsonContentType() {
        return rawJsonContentType;
    }

    public void setRawJsonContentType(String rawJsonContentType) {
        this.rawJsonContentType = rawJsonContentType;
    }
    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public String getLastRunBy() {
        return lastRunBy;
    }

    public void setLastRunBy(String lastRunBy) {
        this.lastRunBy = lastRunBy;
    }
    public Integer getLastRunDur() {
        return lastRunDur;
    }

    public void setLastRunDur(Integer lastRunDur) {
        this.lastRunDur = lastRunDur;
    }
    public LocalDate getLastRunDate() {
        return lastRunDate;
    }

    public void setLastRunDate(LocalDate lastRunDate) {
        this.lastRunDate = lastRunDate;
    }

    public Long getEdmsDownloadId() {
        return edmsDownloadId;
    }

    public void setEdmsDownloadId(Long edmsDownloadId) {
        this.edmsDownloadId = edmsDownloadId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceRespDTO serviceRespDTO = (ServiceRespDTO) o;

        if ( ! Objects.equals(id, serviceRespDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceRespDTO{" +
            "id=" + id +
            ", rawJson='" + rawJson + "'" +
            ", documentImage='" + documentImage + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", lastRunBy='" + lastRunBy + "'" +
            ", lastRunDur='" + lastRunDur + "'" +
            ", lastRunDate='" + lastRunDate + "'" +
            '}';
    }
}
