package ae.etisalat.eim.ocr.gateway.repository.search;

import ae.etisalat.eim.ocr.gateway.domain.RequestWf;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the RequestWf entity.
 */
public interface RequestWfSearchRepository extends ElasticsearchRepository<RequestWf, Long> {
}
