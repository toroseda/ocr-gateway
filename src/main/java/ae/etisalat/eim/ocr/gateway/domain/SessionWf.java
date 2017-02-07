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
    private OcrSession ocrSession;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public SessionWf status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public WfStatus getWfStatus() {
        return wfStatus;
    }

    public SessionWf wfStatus(WfStatus wfStatus) {
        this.wfStatus = wfStatus;
        return this;
    }

    public void setWfStatus(WfStatus wfStatus) {
        this.wfStatus = wfStatus;
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
            ", status='" + status + "'" +
            ", wfStatus='" + wfStatus + "'" +
            ", updatedBy='" + updatedBy + "'" +
            '}';
    }
}
