package com.datahub.metadata.model;

import com.datahub.metadata.utils.Constants;
import com.datahub.metadata.utils.RequestContext;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;

@Document(indexName = Constants.ELASTIC_CUSTOMER_METADATA_INDEX, type = Constants.ELASTIC_CUSTOMER_METADATA_TYPE)
@Setting(settingPath = "/settings/metadata-settings.json")
@Mapping(mappingPath = "/mappings/metadata-mappings.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "func", visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = SourceMetadata.class),
        @JsonSubTypes.Type(value = RawMetadata.class),
        /*@JsonSubTypes.Type(value = CdpMetadata.class),
        @JsonSubTypes.Type(value = TargetMetadata.class)*/
    }
)
public class BaseMetadata implements Serializable {

    private static final long serialVersionUID = 1409715715962049158L;

    @ApiModelProperty(notes = "Attribute unique Id")
    private String id;

    @ApiModelProperty(hidden = true)
    private String company;

    @ApiModelProperty(notes = "Function type of the metadata. System generated value")
    private String func;

    @ApiModelProperty(notes = "Attribute name")
    private String name;

    @ApiModelProperty(notes = "Attribute raw name", hidden = true)
    private String nameRaw;

    @ApiModelProperty(notes = "Attribute description")
    private String desc;

    @ApiModelProperty(notes = "Attribute created datetime in UTC. System generated value when source is created")
    private String createdDateTime;

    @ApiModelProperty(notes = "Attribute modified datetime in UTC. System generated value when source is updated")
    private String modifiedDateTime;

    public BaseMetadata() {
        company = RequestContext.getCompany().toLowerCase();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameRaw() {
        return nameRaw;
    }

    public void setNameRaw(String nameRaw) {
        this.nameRaw = nameRaw;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(String modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }
}

