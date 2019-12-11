package com.datahub.metadata.resources;

import com.datahub.metadata.exception.BadRequestException;
import com.datahub.metadata.model.RawMetadata;
import com.datahub.metadata.model.SourceMetadata;
import com.datahub.metadata.services.RawMetadataService;
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
public class RawMetadataResource {

    private static final Logger LOG = LoggerFactory.getLogger(SourceMetadataResource.class);

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private RawMetadataService rawMetadataService;

    @ApiOperation(value = "Create metadata for source.", response = SourceMetadata.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @PostMapping(value = "/company/{company}/customer/rawData", produces = "application/json")
    @ResponseBody
    public ResponseEntity createRawMetadata(@PathVariable("company") String company,
                                               @RequestBody RawMetadata createRawDataReq) {

        meterRegistry.counter("createRawMetadata Resource").increment();

        RawMetadata responseData;

        try {
            responseData = rawMetadataService.createRawMetaData(createRawDataReq);
        }  catch (BadRequestException bre) {
            LOG.error("Exception while createRawMetadata, exception=", bre);
            return new ResponseEntity<>(bre.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            LOG.error("Exception while createRawMetadata, exception=", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //TODO: Add PUT to update multiple attributes of the raw data object
    //TODO: Add PATCH to update simple attributes like name and description
    //TODO: Add DELETE to archive or complete deletion of the raw data object
}
