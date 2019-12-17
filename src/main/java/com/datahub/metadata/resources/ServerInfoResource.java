package com.datahub.metadata.resources;

import com.datahub.metadata.dto.ServerInfoDto;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value="Version API", description="Rest API to fetch the services version (Doesn't work! placeholder to show the concept)")
@RestController
public class ServerInfoResource {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired(required = false)
    private BuildProperties buildProperties;

    @ApiOperation(value = "Get services version", response = BuildProperties.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @GetMapping(value="/version")
    @ResponseBody
    public ResponseEntity<ServerInfoDto> getVersion() {

        meterRegistry.counter("GetVersion Resource").increment();

        ServerInfoDto serverInfoDto = new ServerInfoDto();
        ServerInfoDto.ServerVersion serverVersion = new ServerInfoDto.ServerVersion();
        if(buildProperties != null) {
            serverVersion.setName(buildProperties.getName());
            serverVersion.setVersion(buildProperties.getVersion());
            serverVersion.setArtifact(buildProperties.getArtifact());
        }
        serverInfoDto.setServerVersion(serverVersion);

        return new ResponseEntity<>(serverInfoDto, HttpStatus.OK);
    }
}

