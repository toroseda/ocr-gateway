package ae.etisalat.eim.ocr.gateway.repository;

import ae.etisalat.eim.ocr.gateway.domain.SessionWf;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SessionWf entity.
 */
@SuppressWarnings("unused")
public interface SessionWfRepository extends JpaRepository<SessionWf,Long> {

}
