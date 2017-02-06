package ae.etisalat.eim.ocr.gateway.repository.search;

import ae.etisalat.eim.ocr.gateway.domain.EdmsLocation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EdmsLocation entity.
 */
public interface EdmsLocationSearchRepository extends ElasticsearchRepository<EdmsLocation, Long> {
}
