package com.datahub.metadata.services;

import com.datahub.metadata.model.BaseMetadata;

public interface MetadataSearchService {

    BaseMetadata getMetaDataById(String id);
}
