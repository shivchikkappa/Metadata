package com.datahub.metadata.resources;

import com.datahub.metadata.exception.NotFoundException;
import com.datahub.metadata.model.BaseMetadata;
import com.datahub.metadata.services.MetadataSearchService;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchMetadataResource {

    private static final Logger LOG = LoggerFactory.getLogger(SearchMetadataResource.class);

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private MetadataSearchService metadataSearchService;

    @ApiOperation(value = "Get metadata", response = BaseMetadata.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @GetMapping(value = "/company/{company}/customer/metadata/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity getMetadata(@PathVariable("company") String company,
                                      @PathVariable("id") String id) {

        meterRegistry.counter("getMetadata Resource").increment();

        BaseMetadata responseData;

        try {
            responseData = metadataSearchService.getMetaDataById(id);
        } catch (NotFoundException bre) {
            LOG.error("Exception while getMetadata, exception=", bre);
            return new ResponseEntity<>(bre.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            LOG.error("Exception while getMetadata, exception=", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
