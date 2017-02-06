package ae.etisalat.eim.ocr.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A EdmsRequest.
 */
@Entity
@Table(name = "edms_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "edmsrequest")
public class EdmsRequest extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @NotNull
    @Column(name = "sub_request_id", nullable = false)
    private String subRequestId;

    @NotNull
    @Column(name = "area_code", nullable = false)
    private String areaCode;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "last_run_by")
    private String lastRunBy;

    @Column(name = "last_run_dur")
    private Integer lastRunDur;

    @Column(name = "last_run_date")
    private LocalDate lastRunDate;

    @ManyToOne
    private OcrSession ocrSession;

    @OneToMany(mappedBy = "edmsRequest")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RequestWf> requestWfs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public EdmsRequest accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSubRequestId() {
        return subRequestId;
    }

    public EdmsRequest subRequestId(String subRequestId) {
        this.subRequestId = subRequestId;
        return this;
    }

    public void setSubRequestId(String subRequestId) {
        this.subRequestId = subRequestId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public EdmsRequest areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public EdmsRequest startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public EdmsRequest endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLastRunBy() {
        return lastRunBy;
    }

    public EdmsRequest lastRunBy(String lastRunBy) {
        this.lastRunBy = lastRunBy;
        return this;
    }

    public void setLastRunBy(String lastRunBy) {
        this.lastRunBy = lastRunBy;
    }

    public Integer getLastRunDur() {
        return lastRunDur;
    }

    public EdmsRequest lastRunDur(Integer lastRunDur) {
        this.lastRunDur = lastRunDur;
        return this;
    }

    public void setLastRunDur(Integer lastRunDur) {
        this.lastRunDur = lastRunDur;
    }

    public LocalDate getLastRunDate() {
        return lastRunDate;
    }

    public EdmsRequest lastRunDate(LocalDate lastRunDate) {
        this.lastRunDate = lastRunDate;
        return this;
    }

    public void setLastRunDate(LocalDate lastRunDate) {
        this.lastRunDate = lastRunDate;
    }

    public OcrSession getOcrSession() {
        return ocrSession;
    }

    public EdmsRequest ocrSession(OcrSession ocrSession) {
        this.ocrSession = ocrSession;
        return this;
    }

    public void setOcrSession(OcrSession ocrSession) {
        this.ocrSession = ocrSession;
    }

    public Set<RequestWf> getRequestWfs() {
        return requestWfs;
    }

    public EdmsRequest requestWfs(Set<RequestWf> requestWfs) {
        this.requestWfs = requestWfs;
        return this;
    }

    public EdmsRequest addRequestWf(RequestWf requestWf) {
        requestWfs.add(requestWf);
        requestWf.setEdmsRequest(this);
        return this;
    }

    public EdmsRequest removeRequestWf(RequestWf requestWf) {
        requestWfs.remove(requestWf);
        requestWf.setEdmsRequest(null);
        return this;
    }

    public void setRequestWfs(Set<RequestWf> requestWfs) {
        this.requestWfs = requestWfs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EdmsRequest edmsRequest = (EdmsRequest) o;
        if (edmsRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, edmsRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EdmsRequest{" +
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
