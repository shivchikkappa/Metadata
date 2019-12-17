package com.datahub.metadata.resources;

import com.datahub.metadata.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseResource {

    protected ResponseEntity buildErrorResponse(HttpStatus status, String message, String moreInfo) {
        ErrorResponse errObj = new ErrorResponse();
        errObj.setStatus(status.value());
        errObj.setMessage(message);
        errObj.setMoreInfo(moreInfo);

        return new ResponseEntity<>(errObj, status);
    }
}
