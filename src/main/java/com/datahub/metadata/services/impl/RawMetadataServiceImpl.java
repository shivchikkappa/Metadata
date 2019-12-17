package com.datahub.metadata.services.impl;

import com.datahub.metadata.dao.CustomerMetadataElasticRepository;
import com.datahub.metadata.exception.BadRequestException;
import com.datahub.metadata.exception.NotFoundException;
import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.model.RawMetadata;
import com.datahub.metadata.model.SourceMetadata;
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
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("RawMetadataService")
public class RawMetadataServiceImpl implements RawMetadataService {

    private static final Logger LOG = LoggerFactory.getLogger(RawMetadataServiceImpl.class);

    @Autowired
    private CustomerMetadataElasticRepository customerMetadataElasticRepository;

    @Autowired
    private MetadataElasticService metadataElasticService;

    @Override
    public RawMetadata createRawMetaData(RawMetadata rawMetadataReq) {

        final Stopwatch timer = Stopwatch.createStarted();

        try {

            validateNotNullFields(rawMetadataReq);

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

            String sourceId = rawMetadataReq.getSourceId();
            if(StringUtils.isNotEmpty(sourceId)) {
                SourceMetadata sourceMetadata = customerMetadataElasticRepository.findSrcDataByCompanyAndIdAndFunc(RequestContext.getCompany(),
                    sourceId, Constants.FUNCTION_SOURCE_DATA);
                if(sourceMetadata == null) {
                    throw new BadRequestException("Invalid sourceId");
                }
            }

            metadataElasticService.insert(rawMetadataReq);

            return (RawMetadata) customerMetadataElasticRepository.findById(rawMetadataReq.getId()).get();

        } finally {
            LOG.info("Time taken in ms to createRawMetaData, time={}", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    @Override
    public void deleteRawMetadata(String id) {

        final Stopwatch timer = Stopwatch.createStarted();

        try {

            RawMetadata existingData = customerMetadataElasticRepository.findRawDataByCompanyAndIdAndFunc
                (RequestContext.getCompany(), id, Constants.FUNCTION_RAW_DATA);

            if(existingData == null) {
                String[] args = {id};
                throw new NotFoundException("Raw medata doesn't exist");
            }

            customerMetadataElasticRepository.deleteById(id);

        } finally {
            LOG.info("Time taken in ms to deleteRawMetadata, time={}", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    private void validateNotNullFields(RawMetadata rawMetadataReq) {

        if(StringUtils.isEmpty(rawMetadataReq.getName())) {
            throw new BadRequestException("Name can't be null");
        }
    }
}
