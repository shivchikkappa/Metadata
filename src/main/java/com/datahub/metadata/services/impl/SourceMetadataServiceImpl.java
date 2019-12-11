package com.datahub.metadata.services.impl;

import com.datahub.metadata.dao.CustomerMetadataElasticRepository;
import com.datahub.metadata.exception.BadRequestException;
import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.model.SourceMetadata;
import com.datahub.metadata.services.MetadataElasticService;
import com.datahub.metadata.services.SourceMetadataService;
import com.datahub.metadata.utils.Constants;
import com.datahub.metadata.utils.RequestContext;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service("SourceMetadataService")
public class SourceMetadataServiceImpl implements SourceMetadataService {

    private static final Logger LOG = LoggerFactory.getLogger(SourceMetadataServiceImpl.class);

    @Autowired
    private CustomerMetadataElasticRepository customerMetadataElasticRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private MetadataElasticService metadataElasticService;

    @Override
    public SourceMetadata createSourceMetaData(SourceMetadata sourceDataReq) {

        final Stopwatch timer = Stopwatch.createStarted();

        try {

            sourceDataReq.setId(metadataElasticService.generateElasticId());
            sourceDataReq.setCreatedDateTime(new DateTime().toString());
            sourceDataReq.setModifiedDateTime(new DateTime().toString());
            sourceDataReq.setNameRaw(sourceDataReq.getName().toLowerCase());
            sourceDataReq.setCompany(RequestContext.getCompany().toLowerCase());

            //Unique name check
            BaseMetadata dataByNameAndFunc = metadataElasticService.getDataByNameAndFunc(sourceDataReq.getName(), Constants.FUNCTION_SOURCE_DATA);
            if(dataByNameAndFunc != null && StringUtils.isNotEmpty(dataByNameAndFunc.getId())) {
              //  String[] args = { sourceMetadata.getName()};
                throw new BadRequestException("Name exists");
            }

           metadataElasticService.insert(sourceDataReq);

           return (SourceMetadata) customerMetadataElasticRepository.findById(sourceDataReq.getId()).get();

        } finally {
            LOG.info("Time taken in ms to createSourceMetaData, time={}", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    private SourceMetadata getSrcDataByName(String name) {

        final Stopwatch timer = Stopwatch.createStarted();

        SourceMetadata sourceMetadata = new SourceMetadata();

        try {

            BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
            queryBuilder.must(matchQuery(Constants.ELASTIC_COMPANY_KEY+Constants.ELASTIC_RAW, RequestContext.getCompany()))
                .must(matchQuery(Constants.ELASTIC_FUNCTION_KEY+Constants.ELASTIC_RAW, Constants.FUNCTION_SOURCE_DATA))
                .must(matchQuery(Constants.ELASTIC_NAME_RAW_KEY, name.toLowerCase()));

            SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(Constants.ELASTIC_CUSTOMER_METADATA_INDEX)
                .withQuery(queryBuilder)
                .build();

            elasticsearchTemplate.queryForPage(searchQuery, SourceMetadata.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                    for (SearchHit searchHit : searchResponse.getHits()) {
                        if (searchResponse.getHits().getHits().length <= 0) {
                            return null;
                        }
                        Map<String, Object> source = searchHit.getSourceAsMap();

                        sourceMetadata.setId((String) source.get(Constants.ELASTIC_ID_KEY));
                        sourceMetadata.setName((String) source.get(Constants.ELASTIC_NAME_KEY));
                    }
                    return null;
                }

                @Override
                public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                    return null;
                }
            });

        } finally {
            LOG.info("Time taken in ms to getSrcDataByName for company={}, name={}, time={}",
                RequestContext.getCompany(),name,timer.elapsed(TimeUnit.MILLISECONDS));
        }

        return sourceMetadata;
    }
}
