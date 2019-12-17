package com.datahub.metadata.resources;

import com.datahub.metadata.exception.BadRequestException;
import com.datahub.metadata.model.TargetMetadata;
import com.datahub.metadata.services.TargetMetaDataService;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.annotations.Api;
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
@Api(value="Company Metadata API", description="Rest API to manage target metadata")
public class TargetMetadataResource extends BaseResource {

    private static final Logger LOG = LoggerFactory.getLogger(TargetMetadataResource.class);

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private TargetMetaDataService targetMetaDataService;

    @ApiOperation(value = "Create metadata for target", response = TargetMetadata.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @PostMapping(value = "/company/{company}/customer/targetData", produces = "application/json")
    @ResponseBody
    public ResponseEntity createTargetMetadata(@PathVariable("company") String company,
                                               @RequestBody TargetMetadata createTargetDataReq) {

        meterRegistry.counter("createTargetMetadata Resource").increment();

        TargetMetadata responseData;

        try {
            responseData = targetMetaDataService.createTargetMetaData(createTargetDataReq);
        }  catch (BadRequestException bre) {
            LOG.error("Exception while createTargetMetadata, exception=", bre);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, bre.getMessage(), "tbd");
        } catch (Exception ex) {
            LOG.error("Exception while createTargetMetadata, exception=", ex);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "tbd");
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @ApiOperation(value = "Update metadata for target (Doesn't work! placeholder to show the concept)", response = TargetMetadata.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @PutMapping(value = "/company/{company}/customer/targetData", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateTargetMetadata(@PathVariable("company") String company,
                                               @RequestBody TargetMetadata updateTargetDataReq) {


        return new ResponseEntity<>("PUT resource", HttpStatus.OK);
    }

    @ApiOperation(value = "Delete metadata for target (Doesn't work! placeholder to show the concept)", response = TargetMetadata.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @DeleteMapping(value = "/company/{company}/customer/targetData/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity DeleteSourceMetadata(@PathVariable("company") String company,
                                               @PathVariable("id") String id) {


        return new ResponseEntity<>("Delete resource", HttpStatus.OK);
    }

    //TODO: Add PUT to update multiple attributes of the source object
    //TODO: Add PATCH to update simple attributes like name and description
    //TODO: Add DELETE to archive or complete deletion of the source
}
