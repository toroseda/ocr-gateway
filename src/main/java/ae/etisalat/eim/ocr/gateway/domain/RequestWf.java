package ae.etisalat.eim.ocr.gateway.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RequestWf.
 */
@Entity
@Table(name = "request_wf")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "requestwf")
public class RequestWf extends AbstractAuditingEntity implements Serializable {

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
    private EdmsRequest edmsRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public RequestWf statusId(Integer statusId) {
        this.statusId = statusId;
        return this;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public RequestWf updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public EdmsRequest getEdmsRequest() {
        return edmsRequest;
    }

    public RequestWf edmsRequest(EdmsRequest edmsRequest) {
        this.edmsRequest = edmsRequest;
        return this;
    }

    public void setEdmsRequest(EdmsRequest edmsRequest) {
        this.edmsRequest = edmsRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestWf requestWf = (RequestWf) o;
        if (requestWf.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, requestWf.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RequestWf{" +
            "id=" + id +
            ", statusId='" + statusId + "'" +
            ", updatedBy='" + updatedBy + "'" +
            '}';
    }
}
