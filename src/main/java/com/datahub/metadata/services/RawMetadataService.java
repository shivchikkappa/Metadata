package com.datahub.metadata.services;

import com.datahub.metadata.model.RawMetadata;

public interface RawMetadataService {

    RawMetadata createRawMetaData(RawMetadata rawMetadata);

    void deleteRawMetadata(String id);

}
