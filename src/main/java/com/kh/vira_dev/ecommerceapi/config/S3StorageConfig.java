package com.kh.vira_dev.ecommerceapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
@ConditionalOnProperty(prefix = "storage", name = "driver", havingValue = "s3")
public class S3StorageConfig {

    @Bean
    S3Client s3Client(
            @Value("${AWS_REGION:${aws.region:}}") String region,
            @Value("${AWS_S3_FORCE_PATH_STYLE:false}") boolean forcePathStyle) {
        if (region.isBlank()) {
            throw new IllegalStateException("AWS_REGION must be configured when STORAGE_DRIVER=s3");
        }

        return S3Client.builder()
                .region(Region.of(region))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(forcePathStyle)
                        .build())
                .build();
    }
}
