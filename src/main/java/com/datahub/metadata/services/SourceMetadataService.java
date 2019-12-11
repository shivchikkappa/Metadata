package com.datahub.metadata.services;

import com.datahub.metadata.model.SourceMetadata;

public interface SourceMetadataService {

    SourceMetadata createSourceMetaData(SourceMetadata sourceDataReq);

}
