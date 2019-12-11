package com.datahub.metadata.services.impl;

import com.datahub.metadata.dao.CustomerMetadataElasticRepository;
import com.datahub.metadata.exception.BadRequestException;
import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.model.RawMetadata;
import com.datahub.metadata.services.MetadataElasticService;
import com.datahub.metadata.services.RawMetadataService;
import com.datahub.metadata.utils.Constants;
import com.datahub.metadata.utils.RequestContext;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("RawMetadataService")
public class RawMetadataServiceImpl implements RawMetadataService {

    private static final Logger LOG = LoggerFactory.getLogger(RawMetadataServiceImpl.class);

    @Autowired
    private CustomerMetadataElasticRepository customerMetadataElasticRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private MetadataElasticService metadataElasticService;

    @Override
    public RawMetadata createRawMetaData(RawMetadata rawMetadataReq) {

        final Stopwatch timer = Stopwatch.createStarted();

        try {

            rawMetadataReq.setId(metadataElasticService.generateElasticId());
            rawMetadataReq.setCreatedDateTime(new DateTime().toString());
            rawMetadataReq.setModifiedDateTime(new DateTime().toString());
            rawMetadataReq.setNameRaw(rawMetadataReq.getName().toLowerCase());
            rawMetadataReq.setCompany(RequestContext.getCompany().toLowerCase());

            //Unique name check
            BaseMetadata dataByNameAndFunc = metadataElasticService.getDataByNameAndFunc(rawMetadataReq.getName(),
                Constants.FUNCTION_RAW_DATA);
            if(dataByNameAndFunc != null && StringUtils.isNotEmpty(dataByNameAndFunc.getId())) {
                throw new BadRequestException("Name exists");
            }

            metadataElasticService.insert(rawMetadataReq);

            return (RawMetadata) customerMetadataElasticRepository.findById(rawMetadataReq.getId()).get();

        } finally {
            LOG.info("Time taken in ms to createRawMetaData, time={}", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }
}
