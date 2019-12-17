package com.datahub.metadata.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MetadataHealthCheck implements HealthIndicator {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataHealthCheck.class);

    @Override
    public Health health() {
        return Health.up().build();
    }
}
