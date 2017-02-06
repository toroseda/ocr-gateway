package ae.etisalat.eim.ocr.gateway.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SessionWf.
 */
@Entity
@Table(name = "session_wf")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sessionwf")
public class SessionWf extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @NotNull
    @Column(name = "wf_type_id", nullable = false)
    private Integer wfTypeId;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    private OcrSession ocrSession;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public SessionWf statusId(Integer statusId) {
        this.statusId = statusId;
        return this;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getWfTypeId() {
        return wfTypeId;
    }

    public SessionWf wfTypeId(Integer wfTypeId) {
        this.wfTypeId = wfTypeId;
        return this;
    }

    public void setWfTypeId(Integer wfTypeId) {
        this.wfTypeId = wfTypeId;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public SessionWf updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public OcrSession getOcrSession() {
        return ocrSession;
    }

    public SessionWf ocrSession(OcrSession ocrSession) {
        this.ocrSession = ocrSession;
        return this;
    }

    public void setOcrSession(OcrSession ocrSession) {
        this.ocrSession = ocrSession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SessionWf sessionWf = (SessionWf) o;
        if (sessionWf.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sessionWf.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SessionWf{" +
            "id=" + id +
            ", statusId='" + statusId + "'" +
            ", wfTypeId='" + wfTypeId + "'" +
            ", updatedBy='" + updatedBy + "'" +
            '}';
    }
}
