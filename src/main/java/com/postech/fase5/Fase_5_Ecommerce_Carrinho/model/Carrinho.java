package com.postech.fase5.Fase_5_Ecommerce_Carrinho.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Carrinho {

    private List<ItemPedido> itensCarrinho;
    private Float totalCarrinho;
    private String userLogin;

    @DynamoDbPartitionKey
    public String getUserLogin(){ return this.userLogin; }



    public static Carrinho fromMap(Map<String, AttributeValue> attributeMap) throws JsonProcessingException {
        Carrinho carrinho = new Carrinho();


        if (attributeMap.containsKey("totalCarrinho")) {
            carrinho.setTotalCarrinho(Float.parseFloat(attributeMap.get("totalCarrinho").s()));
        }

        if (attributeMap.containsKey("userLogin")) {
            carrinho.setUserLogin(attributeMap.get("userLogin").s());
        }

        if (attributeMap.containsKey("itensCarrinho")) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ItemPedido> itensCarrinho = objectMapper.readValue(attributeMap.get("itensCarrinho").s(), new TypeReference<List<ItemPedido>>() {
            });
            carrinho.setItensCarrinho(itensCarrinho);
        }

        return carrinho;
    }

}
