package ae.etisalat.eim.ocr.gateway.repository.search;

import ae.etisalat.eim.ocr.gateway.domain.EdmsResponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EdmsResponse entity.
 */
public interface EdmsResponseSearchRepository extends ElasticsearchRepository<EdmsResponse, Long> {
}
