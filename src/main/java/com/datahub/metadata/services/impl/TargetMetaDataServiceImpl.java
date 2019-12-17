package com.datahub.metadata.services.impl;

import com.datahub.metadata.dao.CustomerMetadataElasticRepository;
import com.datahub.metadata.exception.BadRequestException;
import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.model.TargetMetadata;
import com.datahub.metadata.services.MetadataElasticService;
import com.datahub.metadata.services.TargetMetaDataService;
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

@Service("TargetMetaDataService")
public class TargetMetaDataServiceImpl implements TargetMetaDataService {

    private static final Logger LOG = LoggerFactory.getLogger(TargetMetaDataServiceImpl.class);

    @Autowired
    private CustomerMetadataElasticRepository customerMetadataElasticRepository;

    @Autowired
    private MetadataElasticService metadataElasticService;

    @Override
    public TargetMetadata createTargetMetaData(TargetMetadata targetMetadataReq) {
        final Stopwatch timer = Stopwatch.createStarted();

        try {

            validateNotNullFields(targetMetadataReq);

            targetMetadataReq.setId(metadataElasticService.generateElasticId());
            targetMetadataReq.setCreatedDateTime(new DateTime().toString());
            targetMetadataReq.setModifiedDateTime(new DateTime().toString());
            targetMetadataReq.setNameRaw(targetMetadataReq.getName().toLowerCase());
            targetMetadataReq.setCompany(RequestContext.getCompany().toLowerCase());

            //Unique name check
            BaseMetadata dataByNameAndFunc = metadataElasticService.getDataByNameAndFunc(targetMetadataReq.getName(), Constants.FUNCTION_TARGET_DATA);
            if(dataByNameAndFunc != null && StringUtils.isNotEmpty(dataByNameAndFunc.getId())) {
                throw new BadRequestException("Name exists");
            }

            metadataElasticService.insert(targetMetadataReq);

            return (TargetMetadata) customerMetadataElasticRepository.findById(targetMetadataReq.getId()).get();

        } finally {
            LOG.info("Time taken in ms to createTargetMetaData, time={}", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }


    private void validateNotNullFields(TargetMetadata targetMetadataReq) {

        if(StringUtils.isEmpty(targetMetadataReq.getName())) {
            throw new BadRequestException("Name can't be null");
        }

        if(StringUtils.isEmpty(targetMetadataReq.getType())) {
            throw new BadRequestException("type can't be null");
        }
    }
}
