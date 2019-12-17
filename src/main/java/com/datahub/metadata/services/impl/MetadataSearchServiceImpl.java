package com.datahub.metadata.services.impl;

import com.datahub.metadata.dao.CustomerMetadataElasticRepository;
import com.datahub.metadata.exception.NotFoundException;
import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.model.RawMetadata;
import com.datahub.metadata.services.MetadataSearchService;
import com.datahub.metadata.utils.Constants;
import com.datahub.metadata.utils.RequestContext;
import com.google.common.base.Stopwatch;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service("MetadataSearchService")
public class MetadataSearchServiceImpl implements MetadataSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataSearchServiceImpl.class);

    @Autowired
    private CustomerMetadataElasticRepository customerMetadataElasticRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public BaseMetadata getMetaDataById(String id) {

        final Stopwatch timer = Stopwatch.createStarted();

        try {
            if(customerMetadataElasticRepository.findById(id).isPresent()){
                return  customerMetadataElasticRepository.findById(id).get();
            } else {
                throw new NotFoundException("Metadata by ID doesn't exist");
            }
        } finally {
            LOG.info("Time taken in ms to getMetaDataById, time={}", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    @Override
    public List<RawMetadata> getRawDataBySourceId(String sourceId) {

        final Stopwatch timer = Stopwatch.createStarted();

        try {

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(matchQuery(Constants.ELASTIC_COMPANY_KEY+Constants.ELASTIC_RAW, RequestContext.getCompany()));
            boolQueryBuilder.must(matchQuery("sourceId", sourceId.trim()));

            Sort.Direction sortDirection = Sort.Direction.DESC;
            PageRequest pageRequest = PageRequest.of(0, 100, sortDirection, "modifiedDateTime");

            SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(Constants.ELASTIC_CUSTOMER_METADATA_INDEX)
                .withTypes(Constants.ELASTIC_CUSTOMER_METADATA_TYPE)
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .build();

            List<RawMetadata> rawMetadataList = elasticsearchTemplate.queryForList(searchQuery, RawMetadata.class);

            return rawMetadataList;

        } finally {
            LOG.info("Time taken in ms to getRawDataBySourceId for company={}, id={}, time={}",
                RequestContext.getCompany(),sourceId,timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

}
