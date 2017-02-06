package ae.etisalat.eim.ocr.gateway.repository.search;

import ae.etisalat.eim.ocr.gateway.domain.EdmsDownload;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EdmsDownload entity.
 */
public interface EdmsDownloadSearchRepository extends ElasticsearchRepository<EdmsDownload, Long> {
}
