package com.datahub.metadata.resources;

import com.datahub.metadata.exception.BadRequestException;
import com.datahub.metadata.model.SourceMetadata;
import com.datahub.metadata.services.SourceMetadataService;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SourceMetadataResource {

    private static final Logger LOG = LoggerFactory.getLogger(SourceMetadataResource.class);

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private SourceMetadataService sourceMetadataService;

    @ApiOperation(value = "Create metadata for source.", response = SourceMetadata.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @PostMapping(value = "/company/{company}/customer/sourceData", produces = "application/json")
    @ResponseBody
    public ResponseEntity createSourceMetadata(@PathVariable("company") String company,
                                               @RequestBody SourceMetadata createSourceDataReq) {

        meterRegistry.counter("createSourceMetadata Resource").increment();

        SourceMetadata responseData;

        try {
            responseData = sourceMetadataService.createSourceMetaData(createSourceDataReq);
        }  catch (BadRequestException bre) {
            LOG.error("Exception while postSourceCustomerMetadata, exception=", bre);
            return new ResponseEntity<>(bre.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            LOG.error("Exception while postSourceCustomerMetadata, exception=", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //TODO: Add PUT to update multiple attributes of the source object
    //TODO: Add PATCH to update simple attributes like name and description
    //TODO: Add DELETE to archive or complete deletion of the source
}
