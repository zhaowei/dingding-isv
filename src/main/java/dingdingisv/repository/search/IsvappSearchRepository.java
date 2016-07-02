package dingdingisv.repository.search;

import dingdingisv.domain.Isvapp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Isvapp entity.
 */
public interface IsvappSearchRepository extends ElasticsearchRepository<Isvapp, Long> {
}
