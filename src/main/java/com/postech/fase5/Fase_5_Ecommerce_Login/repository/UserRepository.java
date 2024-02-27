package com.postech.fase5.Fase_5_Ecommerce_Login.repository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Repository
public class UserRepository {

    @Autowired
    private DynamoDbClient dynamodbClient;



    public Optional<User> getUserByLogin(String userLogin) throws Exception {
        try {
            GetItemRequest getItemRequest = GetItemRequest.builder()
                    .tableName("Ecommerce_Login")
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

    public String criarUsuario(com.postech.fase5.Fase_5_Ecommerce_Login.model.User novoUsuario) {

        try{
            Map<String, AttributeValue> itemValues = new HashMap<>();
            itemValues.put("userLogin", AttributeValue.builder().s(novoUsuario.getUserlogin()).build());
            itemValues.put("userKey", AttributeValue.builder().s(novoUsuario.getUserkey()).build());
            itemValues.put("authorities", AttributeValue.builder().s(novoUsuario.getAuthorities()).build());

            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName("Ecommerce_Login")
                    .item(itemValues)
                    .build();

            dynamodbClient.putItem(putItemRequest);
            return novoUsuario.toString();
        }catch(Exception e){
            throw e;
        }
    }

    public String atualizarUsuario(com.postech.fase5.Fase_5_Ecommerce_Login.model.User usuarioAtualizar) {
        try {
            Map<String, AttributeValue> itemKey = new HashMap<>();

            itemKey.put("userLogin", AttributeValue.builder().s(usuarioAtualizar.getUserlogin()).build());

            Map<String, AttributeValue> itemValues = new HashMap<>();
            itemValues.put("userLogin", AttributeValue.builder().s(usuarioAtualizar.getUserlogin()).build());
            itemValues.put("userKey", AttributeValue.builder().s(usuarioAtualizar.getUserkey()).build());
            itemValues.put("authorities", AttributeValue.builder().s(usuarioAtualizar.getAuthorities()).build());

            UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                    .tableName("Ecommerce_Login")
                    .key(itemKey)
                    .build();

            dynamodbClient.updateItem(updateItemRequest);
            return usuarioAtualizar.toString();
        }catch(Exception e){
            throw e;
        }
    }
}
