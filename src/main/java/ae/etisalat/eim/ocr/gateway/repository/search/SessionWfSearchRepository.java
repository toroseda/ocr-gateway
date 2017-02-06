package ae.etisalat.eim.ocr.gateway.repository.search;

import ae.etisalat.eim.ocr.gateway.domain.SessionWf;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SessionWf entity.
 */
public interface SessionWfSearchRepository extends ElasticsearchRepository<SessionWf, Long> {
}
