package com.datahub.metadata.services;

import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.model.RawMetadata;

import java.util.List;

public interface MetadataSearchService {

    BaseMetadata getMetaDataById(String id);

    List<RawMetadata> getRawDataBySourceId(String sourceId);
}
