package ae.etisalat.eim.ocr.gateway.repository;

import ae.etisalat.eim.ocr.gateway.domain.EdmsDownload;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EdmsDownload entity.
 */
@SuppressWarnings("unused")
public interface EdmsDownloadRepository extends JpaRepository<EdmsDownload,Long> {

}
