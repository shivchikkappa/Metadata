package com.datahub.metadata.model;

import com.datahub.metadata.utils.Constants;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

@Document(indexName = Constants.ELASTIC_CUSTOMER_METADATA_INDEX, type = Constants.ELASTIC_CUSTOMER_METADATA_TYPE)
@JsonTypeName(Constants.FUNCTION_SOURCE_DATA)
public class SourceMetadata extends BaseMetadata implements Serializable {

    private static final long serialVersionUID = 1409715715962049158L;

    @ApiModelProperty(notes = "Type of the source; For example ftp,oracle,kafka")
    private String type;

    @ApiModelProperty(notes = "Source rank")
    private Integer rank;

    @ApiModelProperty(notes = "Source state. Valid values are [enabled,disabled,archived]")
    private String state = "disabled";

    @ApiModelProperty(notes = "Source status. System generated value")
    private String status ="inactive";

    @ApiModelProperty(notes = "URI for where the source data file is located. Allowed protocol is FTP. Basic authentication embedded in the URI is supported")
    private String uri;

    @ApiModelProperty(notes = "Source file delimiter. Comma is the default value matching a standard CSV format. Supported values are [\",\" ,\"\\t\", \"|\", \";\"]")
    private String separator = ",";

    @ApiModelProperty(notes = "Username for authentication of source like FTP or accessKey for source type S3")
    private String userName;

    @ApiModelProperty(notes = "Password for authentication of source like FTP or secretAccessKey for source type S3")
    private String password;

    @ApiModelProperty(notes = "Key to identify customer defined userId")
    private String identityKey;

    @ApiModelProperty(notes = "Define the source directory mapping for source. Key is the group name and value is the path to read the file.")
    @Field(type = FieldType.Nested)
    private Map<String, String> groupPath = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    @ApiModelProperty(notes = "Boolean value to identify if data is ingested on source. System generated value")
    private boolean dataIngested;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getState() {
        return state != null && !state.isEmpty() ? state.toLowerCase() : state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status != null && !status.isEmpty() ? status.toLowerCase() : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentityKey() {
        return identityKey;
    }

    public void setIdentityKey(String identityKey) {
        this.identityKey = identityKey;
    }

    public Map<String, String> getGroupPath() {
        return groupPath;
    }

    public void setGroupPath(Map<String, String> groupPath) {
        this.groupPath = groupPath;
    }

    public boolean isDataIngested() {
        return dataIngested;
    }

    public void setDataIngested(boolean dataIngested) {
        this.dataIngested = dataIngested;
    }
}
