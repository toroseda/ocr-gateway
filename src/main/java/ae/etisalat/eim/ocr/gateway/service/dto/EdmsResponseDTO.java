package ae.etisalat.eim.ocr.gateway.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the EdmsResponse entity.
 */
public class EdmsResponseDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String accountNumber;

    @NotNull
    private String subRequestId;

    private String requestDocType;

    private String errorCode;

    private String errorDescription;

    private String promisedDirectory;

    private String responseDocType;

    private String responseSubRequestId;

    private String responseAeaCode;

    private Integer responseDocCount;

    private Integer directoryAvilableFlg;

    private Integer fileCount;


    private Long edmsRequestId;
    
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
    public String getRequestDocType() {
        return requestDocType;
    }

    public void setRequestDocType(String requestDocType) {
        this.requestDocType = requestDocType;
    }
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
    public String getPromisedDirectory() {
        return promisedDirectory;
    }

    public void setPromisedDirectory(String promisedDirectory) {
        this.promisedDirectory = promisedDirectory;
    }
    public String getResponseDocType() {
        return responseDocType;
    }

    public void setResponseDocType(String responseDocType) {
        this.responseDocType = responseDocType;
    }
    public String getResponseSubRequestId() {
        return responseSubRequestId;
    }

    public void setResponseSubRequestId(String responseSubRequestId) {
        this.responseSubRequestId = responseSubRequestId;
    }
    public String getResponseAeaCode() {
        return responseAeaCode;
    }

    public void setResponseAeaCode(String responseAeaCode) {
        this.responseAeaCode = responseAeaCode;
    }
    public Integer getResponseDocCount() {
        return responseDocCount;
    }

    public void setResponseDocCount(Integer responseDocCount) {
        this.responseDocCount = responseDocCount;
    }
    public Integer getDirectoryAvilableFlg() {
        return directoryAvilableFlg;
    }

    public void setDirectoryAvilableFlg(Integer directoryAvilableFlg) {
        this.directoryAvilableFlg = directoryAvilableFlg;
    }
    public Integer getFileCount() {
        return fileCount;
    }

    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }

    public Long getEdmsRequestId() {
        return edmsRequestId;
    }

    public void setEdmsRequestId(Long edmsRequestId) {
        this.edmsRequestId = edmsRequestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EdmsResponseDTO edmsResponseDTO = (EdmsResponseDTO) o;

        if ( ! Objects.equals(id, edmsResponseDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EdmsResponseDTO{" +
            "id=" + id +
            ", accountNumber='" + accountNumber + "'" +
            ", subRequestId='" + subRequestId + "'" +
            ", requestDocType='" + requestDocType + "'" +
            ", errorCode='" + errorCode + "'" +
            ", errorDescription='" + errorDescription + "'" +
            ", promisedDirectory='" + promisedDirectory + "'" +
            ", responseDocType='" + responseDocType + "'" +
            ", responseSubRequestId='" + responseSubRequestId + "'" +
            ", responseAeaCode='" + responseAeaCode + "'" +
            ", responseDocCount='" + responseDocCount + "'" +
            ", directoryAvilableFlg='" + directoryAvilableFlg + "'" +
            ", fileCount='" + fileCount + "'" +
            '}';
    }
}
