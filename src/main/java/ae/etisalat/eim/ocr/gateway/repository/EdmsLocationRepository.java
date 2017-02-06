package ae.etisalat.eim.ocr.gateway.repository;

import ae.etisalat.eim.ocr.gateway.domain.EdmsLocation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EdmsLocation entity.
 */
@SuppressWarnings("unused")
public interface EdmsLocationRepository extends JpaRepository<EdmsLocation,Long> {

}
