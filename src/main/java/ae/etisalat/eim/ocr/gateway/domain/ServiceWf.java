package ae.etisalat.eim.ocr.gateway.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import ae.etisalat.eim.ocr.gateway.domain.enumeration.Status;

import ae.etisalat.eim.ocr.gateway.domain.enumeration.WfStatus;

/**
 * A ServiceWf.
 */
@Entity
@Table(name = "service_wf")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "servicewf")
public class ServiceWf extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "wf_status", nullable = false)
    private WfStatus wfStatus;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    private ServiceResp serviceResp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public ServiceWf status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public WfStatus getWfStatus() {
        return wfStatus;
    }

    public ServiceWf wfStatus(WfStatus wfStatus) {
        this.wfStatus = wfStatus;
        return this;
    }

    public void setWfStatus(WfStatus wfStatus) {
        this.wfStatus = wfStatus;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public ServiceWf updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ServiceResp getServiceResp() {
        return serviceResp;
    }

    public ServiceWf serviceResp(ServiceResp serviceResp) {
        this.serviceResp = serviceResp;
        return this;
    }

    public void setServiceResp(ServiceResp serviceResp) {
        this.serviceResp = serviceResp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceWf serviceWf = (ServiceWf) o;
        if (serviceWf.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serviceWf.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceWf{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", wfStatus='" + wfStatus + "'" +
            ", updatedBy='" + updatedBy + "'" +
            '}';
    }
}
