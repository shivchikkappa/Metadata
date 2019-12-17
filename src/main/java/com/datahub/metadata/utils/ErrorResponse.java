package com.datahub.metadata.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"status", "message", "moreInfo"})
public class ErrorResponse {

    private static final int DEFAULT_STATUS = 400;

    private int status;
    private String message;
    private String moreInfo;

    public ErrorResponse() {
        status = DEFAULT_STATUS;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public ErrorResponse(int status, String message, String moreInfo) {
        this.status = status;
        this.message = message;
        this.moreInfo = moreInfo;
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @JsonProperty
    public int getStatus() {
        return status;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public String getMoreInfo() {
        return moreInfo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorResponse)) return false;

        ErrorResponse errorResponse = (ErrorResponse) o;

        if (status != errorResponse.status) return false;
        if (message != null ? !message.equals(errorResponse.message) : errorResponse.message != null) return false;
        if (moreInfo != null ? !moreInfo.equals(errorResponse.moreInfo) : errorResponse.moreInfo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = status;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (moreInfo != null ? moreInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ErrorResponse [" +
            "status=" + status +
            ", message='" + message + '\'' +
            ", moreInfo='" + moreInfo + '\'' +
            ']';
    }
}
