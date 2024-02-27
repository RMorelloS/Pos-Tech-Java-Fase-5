package com.postech.fase5.Fase_5_Ecommerce_Carrinho.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.model.ApplicationProperties;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.model.Carrinho;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class CarrinhoRepository {

    @Autowired
    private DynamoDbClient dynamodbClient;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UserService userService;


    public UserDetails obterUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails;
    }

    public Carrinho obterCarrinho() throws JsonProcessingException {
        try{
            var usuario = obterUsuarioLogado();
            GetItemRequest getItemRequest = GetItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .key(Map.of("userLogin", AttributeValue.builder().s(usuario.getUsername().toString()).build()))
                    .build();

            GetItemResponse getItemResponse = dynamodbClient.getItem(getItemRequest);

            if (!getItemResponse.hasItem()) {
                return null;
            }

            return Carrinho.fromMap(getItemResponse.item());
        }catch(Exception e){
            throw e;
        }
    }

    public Carrinho criarCarrinho(Carrinho carrinho) {
        try {
            Map<String, AttributeValue> itemValues = new HashMap<>();
            itemValues.put("userLogin", AttributeValue.builder().s(carrinho.getUserLogin()).build());

            ObjectMapper objectMapper = new ObjectMapper();
            String itensCarrinhoJson = objectMapper.writeValueAsString(carrinho.getItensCarrinho());
            itemValues.put("itensCarrinho", AttributeValue.builder().s(String.join(";", itensCarrinhoJson)).build());
            itemValues.put("totalCarrinho", AttributeValue.builder().s(String.valueOf(carrinho.getTotalCarrinho())).build());

            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .item(itemValues)
                    .build();

            dynamodbClient.putItem(putItemRequest);
        } catch (DynamoDbException e) {
            throw e;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return carrinho;
    }


    public Carrinho atualizarCarrinho(Carrinho carrinho) throws JsonProcessingException {
        try{
            Map<String, AttributeValue> itemKey = new HashMap<>();
            itemKey.put("userLogin", AttributeValue.builder().s(carrinho.getUserLogin().toString()).build());

            Map<String, AttributeValueUpdate> itemValues = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            String itensCarrinhoJson = objectMapper.writeValueAsString(carrinho.getItensCarrinho());

            itemValues.put("itensCarrinho", AttributeValueUpdate.builder().
                    value(AttributeValue.builder().s(itensCarrinhoJson).build()).
                    action(AttributeAction.PUT).build());
            itemValues.put("totalCarrinho", AttributeValueUpdate.builder().
                    value(AttributeValue.builder().s(String.valueOf(carrinho.getTotalCarrinho())).build()).
                    action(AttributeAction.PUT).build());

            UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .key(itemKey)
                    .attributeUpdates(itemValues)
                    .build();

            dynamodbClient.updateItem(updateItemRequest);
            return carrinho;
        }catch(Exception e){
            throw e;
        }
    }

    public String limparCarrinho() {
        try {
            DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .key(Map.of("userLogin", AttributeValue.builder().s(userService.getCurrentUser().getName()).build()))
                    .build();
            dynamodbClient.deleteItem(deleteItemRequest);
        }catch(Exception e){
            throw e;
        }
        return "Fatura paga com sucesso!";
    }
}
