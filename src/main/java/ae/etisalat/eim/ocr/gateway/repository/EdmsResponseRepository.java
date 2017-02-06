package ae.etisalat.eim.ocr.gateway.repository;

import ae.etisalat.eim.ocr.gateway.domain.EdmsResponse;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EdmsResponse entity.
 */
@SuppressWarnings("unused")
public interface EdmsResponseRepository extends JpaRepository<EdmsResponse,Long> {

}
