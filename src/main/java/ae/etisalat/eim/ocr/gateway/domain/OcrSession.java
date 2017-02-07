package ae.etisalat.eim.ocr.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ae.etisalat.eim.ocr.gateway.domain.enumeration.Status;

/**
 * A OcrSession.
 */
@Entity
@Table(name = "ocr_session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ocrsession")
public class OcrSession extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "server_file_path")
    private String serverFilePath;

    @NotNull
    @Column(name = "filename", nullable = false)
    private String filename;

    @NotNull
    @Lob
    @Column(name = "request_data", nullable = false)
    private byte[] requestData;

    @Column(name = "request_data_content_type", nullable = false)
    private String requestDataContentType;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "ocrSession")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SessionWf> sessionWfs = new HashSet<>();

    @OneToMany(mappedBy = "ocrSession")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EdmsRequest> edmsRequests = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public OcrSession name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public OcrSession description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public OcrSession status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getServerFilePath() {
        return serverFilePath;
    }

    public OcrSession serverFilePath(String serverFilePath) {
        this.serverFilePath = serverFilePath;
        return this;
    }

    public void setServerFilePath(String serverFilePath) {
        this.serverFilePath = serverFilePath;
    }

    public String getFilename() {
        return filename;
    }

    public OcrSession filename(String filename) {
        this.filename = filename;
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public OcrSession requestData(byte[] requestData) {
        this.requestData = requestData;
        return this;
    }

    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;
    }

    public String getRequestDataContentType() {
        return requestDataContentType;
    }

    public OcrSession requestDataContentType(String requestDataContentType) {
        this.requestDataContentType = requestDataContentType;
        return this;
    }

    public void setRequestDataContentType(String requestDataContentType) {
        this.requestDataContentType = requestDataContentType;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public OcrSession updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<SessionWf> getSessionWfs() {
        return sessionWfs;
    }

    public OcrSession sessionWfs(Set<SessionWf> sessionWfs) {
        this.sessionWfs = sessionWfs;
        return this;
    }

    public OcrSession addSessionWf(SessionWf sessionWf) {
        sessionWfs.add(sessionWf);
        sessionWf.setOcrSession(this);
        return this;
    }

    public OcrSession removeSessionWf(SessionWf sessionWf) {
        sessionWfs.remove(sessionWf);
        sessionWf.setOcrSession(null);
        return this;
    }

    public void setSessionWfs(Set<SessionWf> sessionWfs) {
        this.sessionWfs = sessionWfs;
    }

    public Set<EdmsRequest> getEdmsRequests() {
        return edmsRequests;
    }

    public OcrSession edmsRequests(Set<EdmsRequest> edmsRequests) {
        this.edmsRequests = edmsRequests;
        return this;
    }

    public OcrSession addEdmsRequest(EdmsRequest edmsRequest) {
        edmsRequests.add(edmsRequest);
        edmsRequest.setOcrSession(this);
        return this;
    }

    public OcrSession removeEdmsRequest(EdmsRequest edmsRequest) {
        edmsRequests.remove(edmsRequest);
        edmsRequest.setOcrSession(null);
        return this;
    }

    public void setEdmsRequests(Set<EdmsRequest> edmsRequests) {
        this.edmsRequests = edmsRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OcrSession ocrSession = (OcrSession) o;
        if (ocrSession.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ocrSession.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OcrSession{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            ", serverFilePath='" + serverFilePath + "'" +
            ", filename='" + filename + "'" +
            ", requestData='" + requestData + "'" +
            ", requestDataContentType='" + requestDataContentType + "'" +
            ", updatedBy='" + updatedBy + "'" +
            '}';
    }
}
