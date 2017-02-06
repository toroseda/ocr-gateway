package ae.etisalat.eim.ocr.gateway.repository.search;

import ae.etisalat.eim.ocr.gateway.domain.ServiceWf;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ServiceWf entity.
 */
public interface ServiceWfSearchRepository extends ElasticsearchRepository<ServiceWf, Long> {
}
