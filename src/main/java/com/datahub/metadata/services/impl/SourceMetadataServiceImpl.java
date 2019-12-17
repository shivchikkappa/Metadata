package com.datahub.metadata.services.impl;

import com.datahub.metadata.dao.CustomerMetadataElasticRepository;
import com.datahub.metadata.exception.BadRequestException;
import com.datahub.metadata.exception.NotFoundException;
import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.model.SourceMetadata;
import com.datahub.metadata.services.MetadataElasticService;
import com.datahub.metadata.services.SourceMetadataService;
import com.datahub.metadata.utils.Constants;
import com.datahub.metadata.utils.RequestContext;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("SourceMetadataService")
public class SourceMetadataServiceImpl implements SourceMetadataService {

    private static final Logger LOG = LoggerFactory.getLogger(SourceMetadataServiceImpl.class);

    @Autowired
    private CustomerMetadataElasticRepository customerMetadataElasticRepository;

    @Autowired
    private MetadataElasticService metadataElasticService;

    @Override
    public SourceMetadata createSourceMetaData(SourceMetadata sourceDataReq) {

        final Stopwatch timer = Stopwatch.createStarted();

        try {

            validateNotNullFields(sourceDataReq);

            sourceDataReq.setId(metadataElasticService.generateElasticId());
            sourceDataReq.setCreatedDateTime(new DateTime().toString());
            sourceDataReq.setModifiedDateTime(new DateTime().toString());
            sourceDataReq.setNameRaw(sourceDataReq.getName().toLowerCase());
            sourceDataReq.setCompany(RequestContext.getCompany().toLowerCase());

            //Unique name check
            BaseMetadata dataByNameAndFunc = metadataElasticService.getDataByNameAndFunc(sourceDataReq.getName(), Constants.FUNCTION_SOURCE_DATA);
            if(dataByNameAndFunc != null && StringUtils.isNotEmpty(dataByNameAndFunc.getId())) {
                throw new BadRequestException("Name exists");
            }

           metadataElasticService.insert(sourceDataReq);

           return (SourceMetadata) customerMetadataElasticRepository.findById(sourceDataReq.getId()).get();

        } finally {
            LOG.info("Time taken in ms to createSourceMetaData, time={}", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    @Override
    public SourceMetadata updateSourceDataIngested(String id) {

        final Stopwatch timer = Stopwatch.createStarted();

        try {

            SourceMetadata existingData = customerMetadataElasticRepository.findSrcDataByCompanyAndIdAndFunc
                (RequestContext.getCompany(), id, Constants.FUNCTION_SOURCE_DATA);
            if(existingData == null) {
                String[] args = {id};
                throw new NotFoundException("sourceId doesn't exist");
            }

            metadataElasticService.updateSourceDataIngested(id,true);

            return (SourceMetadata) customerMetadataElasticRepository.findById(id).get();

        } finally {
            LOG.info("Time taken in ms to updateSourceDataIngested, id={}, time={}", id, timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    private void validateNotNullFields(SourceMetadata createSourceDataReq) {

        if(StringUtils.isEmpty(createSourceDataReq.getName())) {
            throw new BadRequestException("Name can't be null");
        }

        if(StringUtils.isEmpty(createSourceDataReq.getType())) {
            throw new BadRequestException("type can't be null");
        }
    }
}
