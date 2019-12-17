package com.datahub.metadata.dao;

import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.model.RawMetadata;
import com.datahub.metadata.model.SourceMetadata;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CustomerMetadataElasticRepository extends ElasticsearchRepository<BaseMetadata, String> {

    SourceMetadata findSrcDataByCompanyAndIdAndFunc(String company, String id, String function);

    RawMetadata findRawDataByCompanyAndIdAndFunc(String company, String id, String function);

}
