package ae.etisalat.eim.ocr.gateway.repository;

import ae.etisalat.eim.ocr.gateway.domain.RequestWf;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RequestWf entity.
 */
@SuppressWarnings("unused")
public interface RequestWfRepository extends JpaRepository<RequestWf,Long> {

}
