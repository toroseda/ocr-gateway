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

/**
 * A EdmsLocation.
 */
@Entity
@Table(name = "edms_location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "edmslocation")
public class EdmsLocation extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "actual_directory", nullable = false)
    private String actualDirectory;

    @NotNull
    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @OneToOne
    @JoinColumn(unique = true)
    private EdmsResponse edmsResponse;

    @OneToMany(mappedBy = "edmsLocation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EdmsDownload> edmsDownloads = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActualDirectory() {
        return actualDirectory;
    }

    public EdmsLocation actualDirectory(String actualDirectory) {
        this.actualDirectory = actualDirectory;
        return this;
    }

    public void setActualDirectory(String actualDirectory) {
        this.actualDirectory = actualDirectory;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public EdmsLocation statusId(Integer statusId) {
        this.statusId = statusId;
        return this;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public EdmsResponse getEdmsResponse() {
        return edmsResponse;
    }

    public EdmsLocation edmsResponse(EdmsResponse edmsResponse) {
        this.edmsResponse = edmsResponse;
        return this;
    }

    public void setEdmsResponse(EdmsResponse edmsResponse) {
        this.edmsResponse = edmsResponse;
    }

    public Set<EdmsDownload> getEdmsDownloads() {
        return edmsDownloads;
    }

    public EdmsLocation edmsDownloads(Set<EdmsDownload> edmsDownloads) {
        this.edmsDownloads = edmsDownloads;
        return this;
    }

    public EdmsLocation addEdmsDownload(EdmsDownload edmsDownload) {
        edmsDownloads.add(edmsDownload);
        edmsDownload.setEdmsLocation(this);
        return this;
    }

    public EdmsLocation removeEdmsDownload(EdmsDownload edmsDownload) {
        edmsDownloads.remove(edmsDownload);
        edmsDownload.setEdmsLocation(null);
        return this;
    }

    public void setEdmsDownloads(Set<EdmsDownload> edmsDownloads) {
        this.edmsDownloads = edmsDownloads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EdmsLocation edmsLocation = (EdmsLocation) o;
        if (edmsLocation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, edmsLocation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EdmsLocation{" +
            "id=" + id +
            ", actualDirectory='" + actualDirectory + "'" +
            ", statusId='" + statusId + "'" +
            '}';
    }
}
