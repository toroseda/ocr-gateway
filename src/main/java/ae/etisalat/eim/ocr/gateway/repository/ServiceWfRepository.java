package ae.etisalat.eim.ocr.gateway.repository;

import ae.etisalat.eim.ocr.gateway.domain.ServiceWf;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ServiceWf entity.
 */
@SuppressWarnings("unused")
public interface ServiceWfRepository extends JpaRepository<ServiceWf,Long> {

}
