package com.postech.fase5.Fase_5_Ecommerce_Itens.repository;


import com.postech.fase5.Fase_5_Ecommerce_Itens.model.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private DynamoDbClient dynamodbClient;

    @Autowired
    private ApplicationProperties applicationProperties;

    public Optional<User> getUserByLogin(String userLogin) throws Exception {
        try {
            GetItemRequest getItemRequest = GetItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabelaLogin())
                    .key(Map.of("userLogin", AttributeValue.builder().s(userLogin).build()))
                    .build();

            GetItemResponse getItemResponse = dynamodbClient.getItem(getItemRequest);

            if (getItemResponse.hasItem()) {

                var userRole = new SimpleGrantedAuthority(getItemResponse.item()
                        .get("authorities").s());
                var authorities = new ArrayList<GrantedAuthority>();
                authorities.add(userRole);
                return Optional.of(new User(getItemResponse.item().get("userLogin").s(),
                        getItemResponse.item().get("userKey").s(),
                        authorities));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw e;
        }
    }

}
