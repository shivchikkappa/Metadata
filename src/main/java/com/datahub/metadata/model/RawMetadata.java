package com.datahub.metadata.model;

import com.datahub.metadata.utils.Constants;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Document(indexName = Constants.ELASTIC_CUSTOMER_METADATA_INDEX, type = Constants.ELASTIC_CUSTOMER_METADATA_TYPE)
@JsonTypeName(Constants.FUNCTION_RAW_DATA)
public class RawMetadata extends BaseMetadata implements Serializable {

    private static final long serialVersionUID = 1409715715962049158L;

    @ApiModelProperty(notes = "Source Id i.e. source.id value from source metadata")
    private String sourceId;

    @ApiModelProperty(notes = "Unique column to be used for 1:M mapping")
    private boolean uniqueKeyColumn;

    @ApiModelProperty(notes = "Boolean flag to mark attribute as PII")
    private boolean pii;

    @ApiModelProperty(notes = "Boolean flag to mark attribute as Hippa")
    private boolean hippa;

    @ApiModelProperty(notes = "CDP Attribute data type")
    private String type;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public boolean isUniqueKeyColumn() {
        return uniqueKeyColumn;
    }

    public void setUniqueKeyColumn(boolean uniqueKeyColumn) {
        this.uniqueKeyColumn = uniqueKeyColumn;
    }

    public boolean isPii() {
        return pii;
    }

    public void setPii(boolean pii) {
        this.pii = pii;
    }

    public boolean isHippa() {
        return hippa;
    }

    public void setHippa(boolean hippa) {
        this.hippa = hippa;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
