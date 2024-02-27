package com.postech.fase5.Fase_5_Ecommerce_Pagamento.repository;

import com.postech.fase5.Fase_5_Ecommerce_Pagamento.model.ApplicationProperties;
import com.postech.fase5.Fase_5_Ecommerce_Pagamento.model.FormaPagamento;
import com.postech.fase5.Fase_5_Ecommerce_Pagamento.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FormaPagamentoRepository {

    @Autowired
    private DynamoDbClient dynamodbClient;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationProperties applicationProperties;

    public FormaPagamento obterFormaPagamento(String apelido) throws Exception {
        try{
            GetItemRequest getItemRequest = GetItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .key(Map.of("apelido", AttributeValue.builder().s(apelido).build(),
                    "userLogin", AttributeValue.builder().s(userService.getCurrentUser().getName()).build()))
                    .build();

            GetItemResponse getItemResponse = dynamodbClient.getItem(getItemRequest);

            if (!getItemResponse.hasItem()) {
                throw new Exception("Item não encontrado!");
            }

            return FormaPagamento.fromMap(getItemResponse.item());
        }catch(Exception e){
            throw e;
        }
    }

    public FormaPagamento cadastrarFormaPagamento(FormaPagamento formaPagamento) {
        try {
            Map<String, AttributeValue> itemValues = new HashMap<>();
            itemValues.put("userLogin", AttributeValue.builder().s(userService.getCurrentUser().getName()).build());
            itemValues.put("apelido", AttributeValue.builder().s(formaPagamento.getApelido()).build());
            itemValues.put("numeroCartao", AttributeValue.builder().s(formaPagamento.getNumeroCartao()).build());
            itemValues.put("CVV", AttributeValue.builder().s(String.valueOf(formaPagamento.getCVV())).build());
            itemValues.put("dataVencimento", AttributeValue.builder().s(formaPagamento.getDataVencimento()).build());

            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .item(itemValues)
                    .build();
            dynamodbClient.putItem(putItemRequest);
        } catch (DynamoDbException e) {
            throw e;
        }
        return formaPagamento;
    }

    public FormaPagamento atualizarFormaPagamento(String apelido, FormaPagamento formaPagamento) {
        try{
            Map<String, AttributeValue> itemKey = new HashMap<>();
            itemKey.put("apelido", AttributeValue.builder().s(apelido).build());
            itemKey.put("userLogin", AttributeValue.builder().s(userService.getCurrentUser().getName()).build());

            Map<String, AttributeValueUpdate> itemValues = new HashMap<>();
            itemValues.put("CVV", AttributeValueUpdate.builder().
                    value(AttributeValue.builder().s(String.valueOf(formaPagamento.getCVV())).build()).
                    action(AttributeAction.PUT).build());
            itemValues.put("numeroCartao", AttributeValueUpdate.builder().
                    value(AttributeValue.builder().s(formaPagamento.getNumeroCartao()).build()).
                    action(AttributeAction.PUT).build());
            itemValues.put("dataVencimento", AttributeValueUpdate.builder().
                    value(AttributeValue.builder().s(formaPagamento.getDataVencimento()).build()).
                    action(AttributeAction.PUT).build());

            UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .key(itemKey)
                    .attributeUpdates(itemValues)
                    .build();

            dynamodbClient.updateItem(updateItemRequest);
            return formaPagamento;
        }catch(Exception e){
            throw e;
        }

    }

    public String deletarFormaPagamento(String apelido) {
        try {
            DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .key(Map.of("userLogin", AttributeValue.builder().s(userService.getCurrentUser().getName()).build(),
                    "apelido", AttributeValue.builder().s(apelido).build()))
                    .build();

            dynamodbClient.deleteItem(deleteItemRequest);
        }catch(Exception e){
            throw e;
        }
        return apelido;

    }
}
