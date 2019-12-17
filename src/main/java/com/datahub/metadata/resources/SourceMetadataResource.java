package com.datahub.metadata.resources;

import com.datahub.metadata.exception.BadRequestException;
import com.datahub.metadata.model.SourceMetadata;
import com.datahub.metadata.services.SourceMetadataService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="Company Metadata API", description="Rest API to manage source metadata")
public class SourceMetadataResource extends BaseResource {

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
            LOG.error("Exception while createSourceMetadata, exception=", bre);
            return buildErrorResponse(HttpStatus.BAD_REQUEST, bre.getMessage(), "tbd");
        } catch (Exception ex) {
            LOG.error("Exception while createSourceMetadata, exception=", ex);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "tbd");
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @ApiOperation(value = "Update Source data ingested flag", response = SourceMetadata.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfull"),
        @ApiResponse(code = 404, message = "Not found"),
        @ApiResponse(code = 500, message = "Server error"),
    })
    @PatchMapping(value = "/company/{company}/customer/source/{id}/dataIngested", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateSourceDataIngested(@PathVariable("company") String company,
                                                   @PathVariable("id") String id) {

        meterRegistry.counter("updateSourceDataIngested Resource").increment();

        SourceMetadata responseData = sourceMetadataService.updateSourceDataIngested(id);

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @ApiOperation(value = "Update metadata for source ( Doesn't work! placeholder to show the concept)", response = SourceMetadata.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @PutMapping(value = "/company/{company}/customer/sourceData", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateSourceMetadata(@PathVariable("company") String company,
                                               @RequestBody SourceMetadata updateSourceDataReq) {


        return new ResponseEntity<>("PUT resource", HttpStatus.OK);
    }

    @ApiOperation(value = "Delete metadata for source ( Doesn't work! placeholder to show the concept)", response = SourceMetadata.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @DeleteMapping(value = "/company/{company}/customer/sourceData/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity DeleteSourceMetadata(@PathVariable("company") String company,
                                               @PathVariable("id") String id) {


        return new ResponseEntity<>("Delete resource", HttpStatus.OK);
    }

}
