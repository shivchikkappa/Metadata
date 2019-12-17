package com.datahub.metadata.model;

import com.datahub.metadata.utils.Constants;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Document(indexName = Constants.ELASTIC_CUSTOMER_METADATA_INDEX, type = Constants.ELASTIC_CUSTOMER_METADATA_TYPE)
@JsonTypeName(Constants.FUNCTION_TARGET_DATA)
public class TargetMetadata extends BaseMetadata implements Serializable {

    private static final long serialVersionUID = 1409715715962049158L;

    @ApiModelProperty(notes = "Type of the target; For example ftp,s3")
    private String type;

    @ApiModelProperty(notes = "Target state. Valid values are [enabled,disabled,archived]")
    private String state = "disabled";

    @ApiModelProperty(notes = "Target status. Valid values are [inactive,active]. System generated value with default value as inactive")
    private String status = "inactive";

    @ApiModelProperty(notes = "URI for where the Target data file to be exported for target like FTP or bucketname for target type S3")
    private String uri;

    @ApiModelProperty(notes = "Username for authentication of target like FTP or accessKey for target type S3")
    private String userName;

    @ApiModelProperty(notes = "Password for authentication of target like FTP or secretAccessKey for target type S3")
    private String password;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
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
}

