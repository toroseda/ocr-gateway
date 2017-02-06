package ae.etisalat.eim.ocr.gateway.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the EdmsRequest entity.
 */
public class EdmsRequestDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String accountNumber;

    @NotNull
    private String subRequestId;

    @NotNull
    private String areaCode;

    private LocalDate startDate;

    private LocalDate endDate;

    private String lastRunBy;

    private Integer lastRunDur;

    private LocalDate lastRunDate;


    private Long ocrSessionId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public String getSubRequestId() {
        return subRequestId;
    }

    public void setSubRequestId(String subRequestId) {
        this.subRequestId = subRequestId;
    }
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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

        EdmsRequestDTO edmsRequestDTO = (EdmsRequestDTO) o;

        if ( ! Objects.equals(id, edmsRequestDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EdmsRequestDTO{" +
            "id=" + id +
            ", accountNumber='" + accountNumber + "'" +
            ", subRequestId='" + subRequestId + "'" +
            ", areaCode='" + areaCode + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", lastRunBy='" + lastRunBy + "'" +
            ", lastRunDur='" + lastRunDur + "'" +
            ", lastRunDate='" + lastRunDate + "'" +
            '}';
    }
}
