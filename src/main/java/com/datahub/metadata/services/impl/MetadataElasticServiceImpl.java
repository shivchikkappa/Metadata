package com.datahub.metadata.services.impl;

import com.datahub.metadata.dao.CustomerMetadataElasticRepository;
import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.services.MetadataElasticService;
import com.datahub.metadata.utils.Constants;
import com.datahub.metadata.utils.RequestContext;
import com.google.common.base.Stopwatch;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service("MetadataElasticService")
public class MetadataElasticServiceImpl implements MetadataElasticService {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataElasticServiceImpl.class);

    @Autowired
    private CustomerMetadataElasticRepository customerMetadataElasticRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void insert(BaseMetadata baseMetadata) {

        final Stopwatch timer = Stopwatch.createStarted();

        try {
            customerMetadataElasticRepository.save(baseMetadata);
        } catch (Exception ex) {
            LOG.error("Failed to insert to elastic search, function={}, name={}, exception={}", baseMetadata.getFunc(), baseMetadata.getName());
            throw new RuntimeException("Failed to insert data to elastic search");
        } finally {
            LOG.info("Time taken in ms to insert, function={}, time={}", baseMetadata.getFunc(), timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public String generateElasticId() {
        return UUID.randomUUID().toString().toUpperCase().replace("-", "");
    }

    @Override
    public BaseMetadata getDataByNameAndFunc(String name, String func) {

        final Stopwatch timer = Stopwatch.createStarted();

        BaseMetadata baseMetadata = new BaseMetadata();

        try {

            BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
            queryBuilder.must(matchQuery(Constants.ELASTIC_COMPANY_KEY+Constants.ELASTIC_RAW, RequestContext.getCompany()))
                .must(matchQuery(Constants.ELASTIC_FUNCTION_KEY+Constants.ELASTIC_RAW, func))
                .must(matchQuery(Constants.ELASTIC_NAME_RAW_KEY, name.toLowerCase()));

            SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(Constants.ELASTIC_CUSTOMER_METADATA_INDEX)
                .withQuery(queryBuilder)
                .build();

            elasticsearchTemplate.queryForPage(searchQuery, BaseMetadata.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                    for (SearchHit searchHit : searchResponse.getHits()) {
                        if (searchResponse.getHits().getHits().length <= 0) {
                            return null;
                        }
                        Map<String, Object> source = searchHit.getSourceAsMap();

                        baseMetadata.setId((String) source.get(Constants.ELASTIC_ID_KEY));
                        baseMetadata.setName((String) source.get(Constants.ELASTIC_NAME_KEY));
                    }
                    return null;
                }

                @Override
                public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                    return null;
                }
            });

        } finally {
            LOG.info("Time taken in ms to getSrcDataByName for company={}, name={}, func={}, time={}",
                RequestContext.getCompany(),name,func,timer.elapsed(TimeUnit.MILLISECONDS));
        }

        return baseMetadata;
    }

    @Override
    public void updateSourceDataIngested(String id, boolean isIngested) {

        final Stopwatch timer = Stopwatch.createStarted();

        try {

            UpdateRequest updateRequest = new UpdateRequest(Constants.ELASTIC_CUSTOMER_METADATA_INDEX,
                Constants.ELASTIC_CUSTOMER_METADATA_TYPE, id).doc(jsonBuilder()
                .startObject()
                .field("dataIngested", isIngested)
                .field("modifiedDateTime", new DateTime().toString())
                .endObject());

            elasticsearchTemplate.getClient().update(updateRequest).get();

        } catch (Exception ex) {
            LOG.error("Failed to updateSourceDataIngested, id={}, exception={}", id, ex);
        } finally {
            LOG.info("Time taken in ms to updateSourceDataIngested, time={}", timer.elapsed(TimeUnit.MILLISECONDS));
        }

    }
}
