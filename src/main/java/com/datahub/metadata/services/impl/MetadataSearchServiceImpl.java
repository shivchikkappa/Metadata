package com.datahub.metadata.services.impl;

import com.datahub.metadata.dao.CustomerMetadataElasticRepository;
import com.datahub.metadata.exception.NotFoundException;
import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.services.MetadataSearchService;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("MetadataSearchService")
public class MetadataSearchServiceImpl implements MetadataSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataSearchServiceImpl.class);

    @Autowired
    private CustomerMetadataElasticRepository customerMetadataElasticRepository;

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
}
