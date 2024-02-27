package com.postech.fase5.Fase_5_Ecommerce_Itens.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Item {



    @NotBlank(message = "ID do item não pode ser vazio!")
    private UUID itemUUID;

    @NotBlank(message = "Preço do item não pode ser vazio!")
    @Min(0)
    private Float precoItem;

    @NotBlank(message = "Nome do item não pode ser vazio!")
    private String nomeItem;

    @NotBlank(message = "Marca do item não pode ser vazio!")
    private String marcaItem;

    @NotBlank(message = "Descricao do item não pode ser vazio!")
    private String descricao;

    @NotBlank(message = "Quantidade em estoque do item não pode ser vazio!")
    @Min(0)
    private int quantidadeEstoque;



    @DynamoDbPartitionKey
    public UUID getItemUUID(){ return this.itemUUID; }



    public static Item fromMap(Map<String, AttributeValue> attributeMap) {
        Item item = new Item();

        if (attributeMap.containsKey("itemId")) {
            item.setItemUUID(UUID.fromString(attributeMap.get("itemId").s()));
        }

        if (attributeMap.containsKey("precoItem")) {
            item.setPrecoItem(Float.parseFloat(attributeMap.get("precoItem").s()));
        }

        if (attributeMap.containsKey("nomeItem")) {
            item.setNomeItem(attributeMap.get("nomeItem").s());
        }
        if (attributeMap.containsKey("marcaitem")) {
            item.setMarcaItem(attributeMap.get("marcaitem").s());
        }

        if (attributeMap.containsKey("descricao")) {
            item.setDescricao(attributeMap.get("descricao").s());
        }
        if (attributeMap.containsKey("quantidadeEstoque")) {
            item.setQuantidadeEstoque(Integer.parseInt(attributeMap.get("quantidadeEstoque").s()));
        }

        return item;
    }


}
