package ae.etisalat.eim.ocr.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ServiceResp.
 */
@Entity
@Table(name = "service_resp")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "serviceresp")
public class ServiceResp extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Lob
    @Column(name = "raw_json", nullable = false)
    private byte[] rawJson;

    @Column(name = "raw_json_content_type", nullable = false)
    private String rawJsonContentType;

    @Column(name = "document_image")
    private String documentImage;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "last_run_by")
    private String lastRunBy;

    @Column(name = "last_run_dur")
    private Integer lastRunDur;

    @Column(name = "last_run_date")
    private ZonedDateTime lastRunDate;

    @OneToOne
    @JoinColumn(unique = true)
    private EdmsDownload edmsDownload;

    @OneToMany(mappedBy = "serviceResp")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceWf> serviceWfs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getRawJson() {
        return rawJson;
    }

    public ServiceResp rawJson(byte[] rawJson) {
        this.rawJson = rawJson;
        return this;
    }

    public void setRawJson(byte[] rawJson) {
        this.rawJson = rawJson;
    }

    public String getRawJsonContentType() {
        return rawJsonContentType;
    }

    public ServiceResp rawJsonContentType(String rawJsonContentType) {
        this.rawJsonContentType = rawJsonContentType;
        return this;
    }

    public void setRawJsonContentType(String rawJsonContentType) {
        this.rawJsonContentType = rawJsonContentType;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public ServiceResp documentImage(String documentImage) {
        this.documentImage = documentImage;
        return this;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ServiceResp startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public ServiceResp endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getLastRunBy() {
        return lastRunBy;
    }

    public ServiceResp lastRunBy(String lastRunBy) {
        this.lastRunBy = lastRunBy;
        return this;
    }

    public void setLastRunBy(String lastRunBy) {
        this.lastRunBy = lastRunBy;
    }

    public Integer getLastRunDur() {
        return lastRunDur;
    }

    public ServiceResp lastRunDur(Integer lastRunDur) {
        this.lastRunDur = lastRunDur;
        return this;
    }

    public void setLastRunDur(Integer lastRunDur) {
        this.lastRunDur = lastRunDur;
    }

    public ZonedDateTime getLastRunDate() {
        return lastRunDate;
    }

    public ServiceResp lastRunDate(ZonedDateTime lastRunDate) {
        this.lastRunDate = lastRunDate;
        return this;
    }

    public void setLastRunDate(ZonedDateTime lastRunDate) {
        this.lastRunDate = lastRunDate;
    }

    public EdmsDownload getEdmsDownload() {
        return edmsDownload;
    }

    public ServiceResp edmsDownload(EdmsDownload edmsDownload) {
        this.edmsDownload = edmsDownload;
        return this;
    }

    public void setEdmsDownload(EdmsDownload edmsDownload) {
        this.edmsDownload = edmsDownload;
    }

    public Set<ServiceWf> getServiceWfs() {
        return serviceWfs;
    }

    public ServiceResp serviceWfs(Set<ServiceWf> serviceWfs) {
        this.serviceWfs = serviceWfs;
        return this;
    }

    public ServiceResp addServiceWf(ServiceWf serviceWf) {
        serviceWfs.add(serviceWf);
        serviceWf.setServiceResp(this);
        return this;
    }

    public ServiceResp removeServiceWf(ServiceWf serviceWf) {
        serviceWfs.remove(serviceWf);
        serviceWf.setServiceResp(null);
        return this;
    }

    public void setServiceWfs(Set<ServiceWf> serviceWfs) {
        this.serviceWfs = serviceWfs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceResp serviceResp = (ServiceResp) o;
        if (serviceResp.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serviceResp.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceResp{" +
            "id=" + id +
            ", rawJson='" + rawJson + "'" +
            ", rawJsonContentType='" + rawJsonContentType + "'" +
            ", documentImage='" + documentImage + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", lastRunBy='" + lastRunBy + "'" +
            ", lastRunDur='" + lastRunDur + "'" +
            ", lastRunDate='" + lastRunDate + "'" +
            '}';
    }
}
