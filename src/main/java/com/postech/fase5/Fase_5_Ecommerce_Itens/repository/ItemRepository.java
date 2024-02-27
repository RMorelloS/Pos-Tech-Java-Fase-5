package com.postech.fase5.Fase_5_Ecommerce_Itens.repository;

import com.postech.fase5.Fase_5_Ecommerce_Itens.model.ApplicationProperties;
import com.postech.fase5.Fase_5_Ecommerce_Itens.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Repository
public class ItemRepository {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private DynamoDbClient dynamodbClient;


    public String criarItem(Item item) {

        try {
            Map<String, AttributeValue> itemValues = new HashMap<>();
            itemValues.put("itemId", AttributeValue.builder().s(item.getItemUUID().toString()).build());
            itemValues.put("precoItem", AttributeValue.builder().s(String.valueOf(item.getPrecoItem())).build());
            itemValues.put("nomeItem", AttributeValue.builder().s(item.getNomeItem().toString()).build());
            itemValues.put("marcaitem", AttributeValue.builder().s(item.getMarcaItem().toString()).build());
            itemValues.put("descricao", AttributeValue.builder().s(item.getDescricao().toString()).build());
            itemValues.put("quantidadeEstoque", AttributeValue.builder().s(String.valueOf(item.getQuantidadeEstoque())).build());


            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .item(itemValues)
                    .build();

            dynamodbClient.putItem(putItemRequest);
        } catch (DynamoDbException e) {
            throw e;
        }
        return item.toString();
    }
    public Item obterItem(UUID idItem) throws Exception {
        try{
            GetItemRequest getItemRequest = GetItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .key(Map.of("itemId", AttributeValue.builder().s(idItem.toString()).build()))
                    .build();

            GetItemResponse getItemResponse = dynamodbClient.getItem(getItemRequest);

            if (!getItemResponse.hasItem()) {
                throw new Exception("Item não encontrado!");
            }

            return Item.fromMap(getItemResponse.item());
        }catch(Exception e){
            throw e;
        }
    }

    public Item obterItemPorId(UUID idItem) throws Exception {
        try{
            return obterItem(idItem);
        }catch(Exception e){
            throw e;
        }
    }

    public Integer obterQtdeItemPorId(UUID idItem) throws Exception {
        try{
            return obterItem(idItem).getQuantidadeEstoque();
        }catch(Exception e){
            throw e;
        }
    }

    public List<Item> obterItens() {
        try{
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .build();

            ScanResponse scanResponse = dynamodbClient.scan(scanRequest);

            ArrayList<Item> listaItens = new ArrayList<>();
            for (Map<String, AttributeValue> itemBusca : scanResponse.items()) {
                listaItens.add(Item.fromMap(itemBusca));
            }
            return listaItens;
        }
        catch(Exception e){
            throw e;
        }
    }

    public String deletarItem(UUID idItem) {
        try {
            DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .key(Map.of("itemId", AttributeValue.builder().s(idItem.toString()).build()))
                    .build();

            dynamodbClient.deleteItem(deleteItemRequest);
        }catch(Exception e){
            throw e;
        }
        return idItem.toString();
    }

    public String atualizarItem(UUID item, Item itemAtualizar) {
        try{
            Map<String, AttributeValue> itemKey = new HashMap<>();
            itemKey.put("itemId", AttributeValue.builder().s(item.toString()).build());

            Map<String, AttributeValueUpdate> itemValues = new HashMap<>();
            itemValues.put("precoItem", AttributeValueUpdate.builder().
                    value(AttributeValue.builder().s(String.valueOf(itemAtualizar.getPrecoItem())).build()).
                    action(AttributeAction.PUT).build());
            itemValues.put("nomeItem", AttributeValueUpdate.builder().
                    value(AttributeValue.builder().s(itemAtualizar.getNomeItem()).build()).
                    action(AttributeAction.PUT).build());
            itemValues.put("marcaItem", AttributeValueUpdate.builder().
                    value(AttributeValue.builder().s(itemAtualizar.getMarcaItem()).build()).
                    action(AttributeAction.PUT).build());
            itemValues.put("descricao", AttributeValueUpdate.builder().
                    value(AttributeValue.builder().s(itemAtualizar.getDescricao()).build()).
                    action(AttributeAction.PUT).build());
            itemValues.put("quantidadeEstoque", AttributeValueUpdate.builder().
                    value(AttributeValue.builder().s(String.valueOf(itemAtualizar.getQuantidadeEstoque())).build()).
                    action(AttributeAction.PUT).build());

            UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                    .tableName(applicationProperties.getNomeTabela())
                    .key(itemKey)
                    .attributeUpdates(itemValues)
                    .build();

            dynamodbClient.updateItem(updateItemRequest);
            return itemAtualizar.toString();
        }catch(Exception e){
            throw e;
        }
    }

    public String obterPrecoItemPorId(UUID idItem) throws Exception {
        try{
            return obterItem(idItem).getPrecoItem().toString();
        }catch(Exception e){
            throw e;
        }
    }
}
