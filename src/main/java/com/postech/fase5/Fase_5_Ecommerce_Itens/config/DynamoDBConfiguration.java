package com.postech.fase5.Fase_5_Ecommerce_Itens.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;

import java.net.URI;
import java.util.UUID;

@Configuration
public class DynamoDBConfiguration {

    @Autowired
    private Environment env;



    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(env.getProperty("aws-region")))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                System.getenv("AWS_ACCESS_KEY_ID"),
                                System.getenv("AWS_SECRET_ACCESS_KEY")
                        )
                ))
                .endpointOverride(URI.create(env.getProperty("aws-service-endpoint")))
                .build();
    }

}