package com.datahub.metadata.dao;

import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.model.SourceMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CustomerMetadataElasticRepository extends ElasticsearchRepository<BaseMetadata, String> {

    Page<BaseMetadata> findMetadataByCompanyAndFunc(String company, String function, Pageable pageable);

    SourceMetadata findSrcDataByCompanyAndIdAndFunc(String company, String id, String function);

}
