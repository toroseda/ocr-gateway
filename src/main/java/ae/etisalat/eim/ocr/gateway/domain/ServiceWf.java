package ae.etisalat.eim.ocr.gateway.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

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
    @Column(name = "status_id", nullable = false)
    private Integer statusId;

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

    public Integer getStatusId() {
        return statusId;
    }

    public ServiceWf statusId(Integer statusId) {
        this.statusId = statusId;
        return this;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
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
            ", statusId='" + statusId + "'" +
            ", updatedBy='" + updatedBy + "'" +
            '}';
    }
}
