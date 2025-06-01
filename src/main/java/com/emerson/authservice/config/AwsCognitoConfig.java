package com.emerson.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class AwsCognitoConfig {

    @Value("${aws.cognito.region}")
    private String awsRegion;

    @Value("${aws.cognito.accessKeyId:}")
    private String accessKeyId;

    @Value("${aws.cognito.secretKey:}")
    private String secretKey;

    @Bean
    public CognitoIdentityProviderClient cognitoClient() {
        var builder = CognitoIdentityProviderClient.builder()
                .region(Region.of(awsRegion));

        if (accessKeyId != null && !accessKeyId.isBlank() &&
                secretKey != null && !secretKey.isBlank()) {
            builder.credentialsProvider(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKeyId, secretKey)
                    )
            );
        }

        return builder.build();
    }
}
