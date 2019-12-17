package com.datahub.metadata.resources;

import com.datahub.metadata.exception.BadRequestException;
import com.datahub.metadata.exception.NotFoundException;
import com.datahub.metadata.model.RawMetadata;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RawMetadataResource extends BaseResource {

    private static final Logger LOG = LoggerFactory.getLogger(RawMetadataResource.class);

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private RawMetadataService rawMetadataService;

    @ApiOperation(value = "Create metadata for Raw data.", response = RawMetadata.class)
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
            return buildErrorResponse(HttpStatus.BAD_REQUEST, bre.getMessage(), "tbd");
        } catch (Exception ex) {
            LOG.error("Exception while createRawMetadata, exception=", ex);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "tbd");
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @ApiOperation(value = "Update metadata for Raw data (Doesn't work! placeholder to show the concept)", response = RawMetadata.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @PutMapping(value = "/company/{company}/customer/rawData", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateRawMetadata(@PathVariable("company") String company,
                                               @RequestBody RawMetadata updateRawDataReq) {


        return new ResponseEntity<>("PUT resource", HttpStatus.OK);
    }

    @ApiOperation(value = "Delete metadata for Raw data", response = RawMetadata.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @DeleteMapping(value = "/company/{company}/customer/rawData/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity DeleteRawMetadata(@PathVariable("company") String company,
                                               @PathVariable("id") String id) {


        meterRegistry.counter("deleteRawMetadata Resource").increment();

        try {
            rawMetadataService.deleteRawMetadata(id);
        }  catch (NotFoundException nfe) {
            LOG.error("Exception while deleteRawMetadata, exception=", nfe);
            return buildErrorResponse(HttpStatus.NOT_FOUND, nfe.getMessage(), "tbd");
        } catch (Exception ex) {
            LOG.error("Exception while deleteRawMetadata, exception=", ex);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "tbd");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    //TODO: Add PUT to update multiple attributes of the raw data object
    //TODO: Add PATCH to update simple attributes like name and description
    //TODO: Add DELETE to archive or complete deletion of the raw data object
}
