package dingdingisv.repository.search;

import dingdingisv.domain.IsvappPermantCode;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the IsvappPermantCode entity.
 */
public interface IsvappPermantCodeSearchRepository extends ElasticsearchRepository<IsvappPermantCode, Long> {
}
