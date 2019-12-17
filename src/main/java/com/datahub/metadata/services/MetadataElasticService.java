package com.datahub.metadata.services;

import com.datahub.metadata.model.BaseMetadata;

public interface MetadataElasticService {

    void insert(BaseMetadata baseMetadata);

    String generateElasticId();

    BaseMetadata getDataByNameAndFunc(String name, String func);

    public void updateSourceDataIngested(String id, boolean isIngested);
}
